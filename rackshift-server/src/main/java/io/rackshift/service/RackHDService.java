package io.rackshift.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import io.rackshift.config.WorkflowConfig;
import io.rackshift.constants.PluginConstants;
import io.rackshift.constants.RackHDConstants;
import io.rackshift.metal.sdk.IMetalProvider;
import io.rackshift.metal.sdk.util.CloudProviderManager;
import io.rackshift.metal.sdk.util.DiskUtils;
import io.rackshift.model.MachineEntity;
import io.rackshift.model.RackHDResponse;
import io.rackshift.model.WorkflowResponse;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.EndpointMapper;
import io.rackshift.strategy.statemachine.LifeStatus;
import io.rackshift.utils.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RackHDService {
    @Resource
    private CloudProviderManager metalProviderManager;
    @Value("${run.mode:local}")
    private String runMode;
    @Resource
    private EndpointMapper endpointMapper;

    private JSONArray getCatalogs(String nodeId) {
        JSONArray arr = new JSONArray();

        FindIterable<Document> catalogsR = MongoUtil.find("catalogs", new BasicDBObject("node", new ObjectId(nodeId)));
        for (Document document : catalogsR) {
            arr.add(JSONObject.toJSON(document));
        }
        return arr;
    }

    public JSONArray getLocalNodes() {
        JSONArray arr = new JSONArray();
        String nodes = "nodes";
        FindIterable<Document> nodesR = MongoUtil.find(nodes, new BasicDBObject());
        for (Document document : nodesR) {
            JSONObject node = (JSONObject) JSONObject.toJSON(document);
            node.put("id", document.get("_id").toString());
            arr.add(node);
        }
        return arr;
    }

    public Pager<JSONArray> getGraphDefinitions(String name, int page, int pageSize) {
        String collections = "graphdefinitions";
        Pattern pattern = Pattern.compile(".*" + name + ".*", Pattern.CASE_INSENSITIVE);
        List<BasicDBObject> cond = new ArrayList<BasicDBObject>() {{
            add(new BasicDBObject("friendlyName", pattern));
            add(new BasicDBObject("injectableName", pattern));
        }};
        if (StringUtils.isNotBlank(name)) {
            return MongoUtil.page(collections, new BasicDBObject("$or", cond), page, pageSize);
        }
        return MongoUtil.page(collections, new BasicDBObject(), page, pageSize);
    }

    /**
     * 更新IPMI账号密码 ip
     *
     * @param o
     * @param bareMetal
     * @return
     */
    public boolean createOrUpdateObm(OutBand o, BareMetal bareMetal) {
        if (!StringUtils.isAnyBlank(bareMetal.getEndpointId(), bareMetal.getServerId())) {
            try {
                JSONObject obmObj = new JSONObject();
                JSONObject obmConfigObj = new JSONObject();
                obmConfigObj.put("host", o.getIp());
                obmConfigObj.put("user", o.getUserName());
                obmConfigObj.put("password", o.getPwd());

                obmObj.put("config", obmConfigObj);
                obmObj.put("service", "ipmi-obm-service");
                obmObj.put("nodeId", bareMetal.getServerId());

                String url = WorkflowConfig.geRackhdUrlById(bareMetal.getEndpointId());
                if (url == null) {
                    return true;
                }
                if (url.endsWith("/")) {
                    url = url.substring(0, url.length() - 1);
                }

                RackHDHttpClientUtil.put(String.format(url + RackHDConstants.NODES_OBM_URL, bareMetal.getServerId()), obmObj.toJSONString(), null);
                return true;
            } catch (Exception e) {
                return false;
                // 同步至RackHD失败！
            }
        }
        return false;
    }

    /**
     * 清除所有正在跑的workflow
     *
     * @param bareMetal
     * @return
     */
    public boolean clearActiveWorkflow(BareMetal bareMetal) {
        if (!StringUtils.isAnyBlank(bareMetal.getEndpointId(), bareMetal.getServerId())) {
            try {
                JSONObject param = new JSONObject();
                param.put("command", "cancel");
                param.put("options", JSONObject.parse("{}"));

                String url = WorkflowConfig.geRackhdUrlById(bareMetal.getEndpointId());
                if (url == null) {
                    return false;
                }
                if (url.endsWith("/")) {
                    url = url.substring(0, url.length() - 1);
                }
                RackHDHttpClientUtil.put(String.format(url + RackHDConstants.CANCEL_WORKFLOW_URL, bareMetal.getServerId()), param.toJSONString(), null);
                return true;
            } catch (Exception e) {
                // 同步至RackHD失败！
            }
        }
        return false;
    }

    /**
     * 装系统
     *
     * @param nodeId
     * @param url
     * @param raidAndInstallOsPayLoad
     * @return
     */
    public boolean executeInstallOS(String nodeId, String url, String raidAndInstallOsPayLoad, String os) {
        try {
            //对应的执行任务的id
            RackHDResponse response = RackHDHttpClientUtil.post(String.format(url + getInstallWorkFlow(os), nodeId), raidAndInstallOsPayLoad);
            if (response.getReCode() > RackHDConstants.ERROR_RE_CODE) {
                throw new RuntimeException("装机失败!" + response.getData());
            }
            String workflowId = (JSONObject.parseObject(response.getData())).getString("instanceId");
            WorkflowResponse workflowResponse = getWorkflowResponse(url, workflowId);

            if (!workflowResponse.isSuccess()) {
                throw new RuntimeException("装机失败!");
            }
        } catch (Exception e) {
            throw new RuntimeException("装机失败！" + ExceptionUtils.getExceptionDetail(e));
        }
        return true;
    }

    /**
     * 先做RAID再装系统
     *
     * @param url
     * @param raidPayload
     * @param raidAndInstallOsPayLoad
     * @return
     */
    public boolean executeRaidAndThenInstallOS(BareMetal pm, String url, String raidPayload, String raidAndInstallOsPayLoad, String os) {
        IMetalProvider iMetalProvider = metalProviderManager.getCloudProvider(PluginConstants.PluginType.getPluginByBrand(pm.getMachineBrand()));
        RackHDResponse response = RackHDHttpClientUtil.post(String.format(url + iMetalProvider.getRaidWorkFlow(), pm.getServerId()), raidPayload);
        if (response.getReCode() > RackHDConstants.ERROR_RE_CODE) {
            throw new RuntimeException("做RAID失败!" + response.getData());
        }
        try {
            //对应的执行任务的id
            String workflowId = (JSONObject.parseObject(response.getData())).getString("instanceId");
            WorkflowResponse raidResponse = getWorkflowResponse(url, workflowId);
            if (raidResponse.isSuccess()) {
                executeInstallOS(pm.getServerId(), url, raidAndInstallOsPayLoad, os);
            } else {
                throw new RuntimeException("安装OS失败！");
            }
        } catch (Exception e) {
            throw new RuntimeException("安装OS失败！" + ExceptionUtils.getExceptionDetail(e));
        }
        return true;
    }

    /**
     * 创建raid
     *
     * @param physicalMachine
     * @param url
     * @param raidPayload
     * @return
     */
    public boolean createRaidConfig(BareMetal physicalMachine, String url, String raidPayload) throws Exception {
        try {
            String nodeId = physicalMachine.getServerId();
            IMetalProvider iMetalProvider = metalProviderManager.getCloudProvider(PluginConstants.PluginType.getPluginByBrand(physicalMachine.getMachineBrand()));
            RackHDResponse response = RackHDHttpClientUtil.post(String.format(url + iMetalProvider.getRaidWorkFlow(), nodeId), raidPayload);
            if (response.getReCode() > RackHDConstants.ERROR_RE_CODE) {
                throw new RuntimeException("做RAID失败!" + response.getData());
            }
            String workflowId = (JSONObject.parseObject(response.getData())).getString("instanceId");
            WorkflowResponse workflowResponse = getWorkflowResponse(url, workflowId);

            if (!workflowResponse.isSuccess()) {
                throw new RuntimeException("做RAID失败!");
            }
        } catch (Exception e) {
            throw new RuntimeException("做RAID失败!" + ExceptionUtils.getExceptionDetail(e));
        }
        return true;
    }

    /**
     * RAID 擦除
     *
     * @param url
     * @param deleteRaidPayload
     * @return
     */

    public boolean deleteRaidConfig(BareMetal pm, String url, String deleteRaidPayload) {
        try {
            IMetalProvider iMetalProvider = metalProviderManager.getCloudProvider(PluginConstants.PluginType.getPluginByBrand(pm.getMachineBrand()));
            RackHDResponse response = RackHDHttpClientUtil.post(String.format(url + iMetalProvider.getDeleteRaidWorkFlow(), pm.getServerId()), deleteRaidPayload);
            if (response.getReCode() > RackHDConstants.ERROR_RE_CODE) {
                throw new RuntimeException("RAID擦除失败!" + response.getData());
            }
            String workflowId = (JSONObject.parseObject(response.getData())).getString("instanceId");
            WorkflowResponse workflowResponse = getWorkflowResponse(url, workflowId);

            if (!workflowResponse.isSuccess()) {
                throw new RuntimeException("RAID擦除失败!");
            }
        } catch (Exception e) {
            throw new RuntimeException("RAID擦除失败！" + ExceptionUtils.getExceptionDetail(e));
        }
        return true;
    }

    /**
     * 某些机型经过第一次主动发现 获取磁盘信息失败了 需要手动重新获取一次磁盘catalog
     *
     * @param url
     * @param nodeId
     * @param brand
     * @return
     */
    public boolean needPerccliCatalog(String url, String nodeId, String brand) {
        WorkflowResponse workflowResponse;

        try {
            IMetalProvider iMetalProvider = metalProviderManager.getCloudProvider(PluginConstants.PluginType.getPluginByBrand(brand));
            RackHDResponse response = RackHDHttpClientUtil.post(String.format(url + iMetalProvider.getCatalogRaidWorkFlow(), nodeId), "");
            if (response.getReCode() > RackHDConstants.ERROR_RE_CODE) {
                throw new RuntimeException(String.format("同步时获取dell：「%s」磁盘信息失败！response「%s」", nodeId, response.getData()));
            }
            //对应的执行任务的id
            String workflowId = (JSONObject.parseObject(response.getData())).getString("instanceId");
            workflowResponse = getWorkflowResponse(url, workflowId);
        } catch (Exception e) {
            throw new RuntimeException("获取磁盘信息失败！" + ExceptionUtils.getExceptionDetail(e));
        }
        return workflowResponse.isSuccess();
    }

    /**
     * 开、关机、重启操作
     *
     * @param url
     * @param nodeId
     * @param workflow
     * @return
     */
    public boolean power(String url, String nodeId, String workflow) {
        RackHDResponse response = RackHDHttpClientUtil.post(String.format(url + RackHDConstants.WorkFlowEnum.findByWorkFlow(workflow).getWorkflowUrl(), nodeId), "");
        if (response.getReCode() > RackHDConstants.ERROR_RE_CODE) {
            throw new RuntimeException("操作失败！" + response.getData());
        }
        String workflowId = (JSONObject.parseObject(response.getData())).getString("instanceId");
        try {
            WorkflowResponse workflowResponse = getWorkflowResponse(url, workflowId);
            return workflowResponse.isSuccess();
        } catch (Exception e) {
            throw new RuntimeException("操作失败！" + ExceptionUtils.getExceptionDetail(e));
        }
    }

    /**
     * 获取workflow状态和返回值
     *
     * @param url
     * @param workflowId
     * @return
     * @throws Exception
     */
    public WorkflowResponse getWorkflowResponse(String url, String workflowId) throws Exception {
        WorkflowResponse workflowResponse = new WorkflowResponse();
        workflowResponse.setEnd(false);
        workflowResponse.setSuccess(false);
        if (StringUtils.isEmpty(workflowId)) {
            return workflowResponse;
        }
        //超时时间控制
        int count = 120;
        while (!workflowResponse.isEnd()) {
            Thread.sleep(1000 * 6);
            count--;
            if (count == 0) {
                workflowResponse.setEnd(true);
                break;
            }
            JSONObject workflow = JSONObject.parseObject(HttpClientUtil.get(url + RackHDConstants.GET_WORKFLOW_URL + "/" + workflowId, null));
            if (workflow.getString("instanceId").equalsIgnoreCase(workflowId)) {
                if ("succeeded".equalsIgnoreCase(workflow.getString("status"))) {
                    workflowResponse.setEnd(true);
                    workflowResponse.setSuccess(true);
                    break;
                }
                //如果是安装操作系统超时的情况
                if ("failed".equalsIgnoreCase(workflow.getString("status")) && workflow.getString("injectableName").contains("Graph.Install")) {
                    JSONArray tasks = workflow.getJSONArray("tasks");
                    if (tasks.size() > 0) {
                        for (int i = 0; i < tasks.size(); i++) {
                            JSONObject taskObj = tasks.getJSONObject(i);
                            if ("install-os".equalsIgnoreCase(taskObj.getString("label"))) {
                                if ("succeeded".equalsIgnoreCase(taskObj.getString("state"))) {
//                                    orderService.saveOrderItemLog(orderThreadLocal.get().get("orderItemId"), orderThreadLocal.get().get("physicalId"), "操作系统安装成功！但是网卡IP配置不正确！请管理员手动登录控制台检查！", null, true);
                                    workflowResponse.setSuccess(true);
                                    break;
                                }
                            }
                        }
                    }
                }
                if ("failed".equalsIgnoreCase(workflow.getString("status")) || "cancelled".equalsIgnoreCase(workflow.getString("status"))) {
                    workflowResponse.setEnd(true);
                    break;
                }
            }
            Thread.sleep(1000 * 30);
        }
        return workflowResponse;
    }

    private String getInstallWorkFlow(String os) {
        switch (os) {
            case "windows":
                return RackHDConstants.WorkFlowEnum.INSTALL_WINDOWS.getWorkflowUrl();
            case "centos":
                return RackHDConstants.WorkFlowEnum.INSTALL_CENTOS.getWorkflowUrl();
            default:
                return RackHDConstants.WorkFlowEnum.INSTALL_CENTOS.getWorkflowUrl();
        }
    }

    public MachineEntity getNodeEntity(String ip, String nodeId, String request) {
        if (StringUtils.isBlank(ip)) {
            LogUtil.error(String.format("执行RackHD更新硬件时 rackhdhost/为空!nodeId:%s", nodeId));
            return null;
        }
        if (ip.endsWith("/")) {
            ip = ip.substring(0, ip.length() - 1);
        }

        long memorys = 0l;
        List<Disk> disks = new LinkedList<>();
        List<Cpu> cpus = new LinkedList<>();
        List<Memory> memories = new LinkedList<>();

        MachineEntity en = new MachineEntity();
        memorys = 0L;
        disks = new LinkedList<>();
        cpus = new LinkedList<>();
        memories = new LinkedList<>();

        List<NetworkCard> pmNetworkCards = new ArrayList<>();
        JSONArray catalogsObj = null;
        if ("release".equalsIgnoreCase(runMode)) {
            catalogsObj = JSONArray.parseArray(RackHDHttpClientUtil.get(ip + String.format(RackHDConstants.CATALOG_URL, nodeId), null));
        } else {
            catalogsObj = getCatalogs(nodeId);
        }
        for (int j = 0; j < catalogsObj.size(); j++) {
            JSONObject catalogObj = catalogsObj.getJSONObject(j);
            if ("dmi".equalsIgnoreCase(catalogObj.getString("source"))) {
                JSONObject dmi = catalogObj.getJSONObject("data");
                JSONArray memoryDeviceObj = dmi.getJSONArray("Memory Device");
                String memoryType = null;
                Map<String, String> extendInfo = new HashMap<>();
                List<JSONObject> memoryDetails = new ArrayList<>();
                for (int k = 0; k < memoryDeviceObj.size(); k++) {
                    JSONObject memoryObj = memoryDeviceObj.getJSONObject(k);
                    if (!"No Module Installed".equalsIgnoreCase(memoryObj.getString("Size"))) {
                        memoryType = memoryObj.getString("Type");

                        int memorySize = 0;
                        try {
                            memorySize = DiskSizeUtils.getSize(memoryObj.getString("Size"), DiskSizeUtils.Unit.GB).intValue();
                        } catch (Exception e) {
                            LogUtil.error(String.format("执行RackHD更新硬件时 内存获取失败!nodeId:%s", nodeId));
                        }

                        memorys += memorySize;
                        JSONObject memoryDetail = new JSONObject();
                        memoryDetail.put("memorySize", memorySize);
                        memoryDetail.put("memoryType", memoryType);
                        memoryDetails.add(memoryDetail);

                        //内存
                        Memory memory1 = new Memory();
                        memory1.setMemModType(memoryObj.getString("Type"));
                        memory1.setSyncTime(System.currentTimeMillis());
                        memory1.setMemModSize(String.valueOf(memorySize));
                        memory1.setMemModFrequency(memoryObj.getString("Speed").replace("MHz", ""));
                        if (memoryObj.containsKey("Minimum voltage")) {
                            memory1.setMemModMinVolt(memoryObj.getString("Minimum voltage").replace("V", "").replace(" ", ""));
                        }
                        //内存位置信息
                        if (memoryObj.containsKey("Locator") && memoryObj.getString("Locator").contains("PROC") && memoryObj.getString("Locator").contains("DIMM")) {
                            memory1.setMemCpuNum(memoryObj.getString("Locator").split("DIMM")[0].replace("PROC ", "").replace(" ", ""));
                            memory1.setMemModNum(memoryObj.getString("Locator").split("DIMM")[1].replace("PROC ", "").replace(" ", ""));
                        }
                        if (memoryObj.containsKey("Serial Number")) {
                            memory1.setSn(memoryObj.getString("Serial Number"));
                        }
                        memory1.setMemModPartNum(memoryObj.getString("Part Number"));
                        memories.add(memory1);
                    }
                }
                extendInfo.put("memoryDetails", memoryDetails.toString());
                en.setExtendInfo(extendInfo);
                en.setMemoryType(memoryType);
                JSONObject chassisInfo = dmi.getJSONObject("Chassis Information");
                en.setBrand(chassisInfo.getString("Manufacturer"));
                if ("Dell Inc.".equalsIgnoreCase(en.getBrand())) {
                    en.setBrand(PluginConstants.DELL);
                }
                en.setModel(dmi.getJSONObject("System Information").getString("Product Name"));
                en.setInstanceUuid(dmi.getJSONObject("System Information").getString("UUID"));

                en.setSerialNo(chassisInfo.getString("Serial Number"));
                if ("Inspur".equalsIgnoreCase(en.getBrand())) {
                    en.setSerialNo(dmi.getJSONObject("System Information").getString("Serial Number"));
                }
                //cpu

                Object processor = dmi.getObject("Processor Information", Object.class);
                if (processor instanceof JSONArray) {
                    JSONArray cpuInfo = (JSONArray) processor;
                    en.setCpu(cpuInfo.size());
                    en.setCore(Optional.ofNullable(cpuInfo.getJSONObject(0).getInteger("Core Count")).orElse(1));
                    en.setThread(Optional.ofNullable(cpuInfo.getJSONObject(0).getInteger("Thread Count")).orElse(2));
                    en.setCpuFre(cpuInfo.getJSONObject(0).getString("Current Speed"));
                    en.setCpuType(cpuInfo.getJSONObject(0).getString("Version"));

                    for (int l = 0; l < cpuInfo.size(); l++) {
                        if ("Not Specified".equalsIgnoreCase(cpuInfo.getJSONObject(l).getString("Version"))) {
                            continue;
                        }
                        Cpu cpu = new Cpu();
                        cpu.setProcNumThreads(cpuInfo.getJSONObject(l).getString("Thread Count"));
                        cpu.setSyncTime(System.currentTimeMillis());
                        cpu.setProcSpeed(cpuInfo.getJSONObject(l).getString("Current Speed").replace("MHz", "").replace(" ", ""));
                        cpu.setProcSocket(cpuInfo.getJSONObject(l).getString("Socket Designation").replace("Proc", "").replace("CPU", "").replace(" ", ""));
                        cpu.setProcNumCoresEnabled(cpuInfo.getJSONObject(l).getString("Core Enabled"));
                        cpu.setProcNumCores(cpuInfo.getJSONObject(l).getString("Core Count"));
                        cpu.setProcName(cpuInfo.getJSONObject(l).getString("Version"));
                        cpus.add(cpu);
                    }
                } else {
                    JSONObject cpuInfo = (JSONObject) processor;
                    en.setCpu(1);
                    en.setCore(Optional.ofNullable(cpuInfo.getInteger("Core Count")).orElse(1));
                    en.setThread(Optional.ofNullable(cpuInfo.getInteger("Thread Count")).orElse(2));
                    en.setCpuFre(cpuInfo.getString("Current Speed"));
                    en.setCpuType(cpuInfo.getString("Version"));

                    Cpu cpu = new Cpu();
                    cpu.setProcNumThreads(cpuInfo.getString("Thread Count"));
                    cpu.setSyncTime(System.currentTimeMillis());
                    cpu.setProcSpeed(cpuInfo.getString("Current Speed").replace("MHz", "").replace(" ", ""));
                    cpu.setProcSocket(cpuInfo.getString("Socket Designation").replace("Proc", "").replace("CPU", "").replace(" ", ""));
                    cpu.setProcNumCoresEnabled(cpuInfo.getString("Core Enabled"));
                    cpu.setProcNumCores(cpuInfo.getString("Core Count"));
                    cpu.setProcName(cpuInfo.getString("Version"));
                    cpus.add(cpu);
                }
            }

            if ("bmc".equalsIgnoreCase(catalogObj.getString("source"))) {
                JSONObject bmcData = catalogObj.getJSONObject("data");
                en.setBmcIp(bmcData.getString("IP Address"));
                en.setBmcMac(bmcData.getString("MAC Address"));
            }
            //兼容浪潮8480M4
            if ("0.0.0.0".equalsIgnoreCase(en.getBmcIp()) && "bmc-8".equalsIgnoreCase(catalogObj.getString("source"))) {
                JSONObject bmcData = catalogObj.getJSONObject("data");
                en.setBmcIp(bmcData.getString("IP Address"));
                en.setBmcMac(bmcData.getString("MAC Address"));
            }
            //dell等使用megaraid卡的机型磁盘
            if ("megaraid-controllers".equalsIgnoreCase(catalogObj.getString("source"))) {
                JSONObject diskData = catalogObj.getJSONObject("data");
                JSONArray controllers = diskData.getJSONArray("Controllers");

                for (int k = 0; k < controllers.size(); k++) {
                    JSONObject conObj = controllers.getJSONObject(k);
                    if (conObj.containsKey("Response Data")) {
                        JSONObject pdInfo = conObj.getJSONObject("Response Data");
                        JSONArray pdList = pdInfo.getJSONArray("PD LIST");
                        JSONArray vdList = pdInfo.getJSONArray("VD LIST");
                        // DG / VirtualDisk  0 : VD0 , 1: VD1
                        Map<String, JSONObject> vdMap = new HashMap<>();
                        if (CollectionUtils.isNotEmpty(vdList)) {
                            vdList.forEach(v -> {
                                vdMap.put(((JSONObject) v).getString("DG/VD").split("/")[0], (JSONObject) v);
                            });
                        }
                        for (int l = 0; l < pdList.size(); l++) {
                            JSONObject pdObj = pdList.getJSONObject(l);
                            Disk pd = new Disk();
                            String eId = pdObj.getString("EID:Slt").split(":")[0];
                            //有些机型出厂不自带 RAID 卡 而是共用或者拆机使用，这个值获取不到
                            pd.setEnclosureId(StringUtils.isBlank(eId)? 32 : Integer.valueOf(eId));
                            //暂时只支持一块raid卡 所以写死为0
                            pd.setControllerId(0);
                            pd.setDrive(pdObj.getString("EID:Slt").split(":")[1]);
                            pd.setRaid(vdMap.get(pdObj.getString("DG")) == null ? null : vdMap.get(pdObj.getString("DG")).getString("TYPE"));
                            pd.setSize(DiskUtils.getDiskManufactorValue(pdObj.getString("Size")));
                            pd.setType(pdObj.getString("Intf"));
                            pd.setVirtualDisk(vdMap.get(pdObj.getString("DG")) == null ? null : vdMap.get(pdObj.getString("DG")).getString("Name"));
                            pd.setSyncTime(catalogObj.getDate("updatedAt").getTime());

                            disks.add(pd);
                        }
                    }
                }
            }
            //浪潮磁盘
            if ("adaptecraid-controllers".equalsIgnoreCase(catalogObj.getString("source"))) {
                JSONArray diskData = catalogObj.getJSONArray("data");
                for (int l = 0; l < diskData.size(); l++) {
                    JSONObject pdObj = diskData.getJSONObject(l);
                    Disk pd = new Disk();
                    //暂时只支持一块raid卡 所以写死为1
                    pd.setControllerId(1);
                    pd.setRaid(pdObj.getString("RaidLevel"));
                    pd.setSize((int) Math.ceil(Integer.valueOf(pdObj.getString("Total Size").replace("MB", "").replace(" ", "")) / 1024 / 0.931) + " GB");
                    pd.setType("NO".equalsIgnoreCase(pdObj.getString("SSD")) ? "HDD" : "SSD");
                    pd.setManufactor(pdObj.getString("Vendor"));
                    pd.setEnclosureId(Integer.valueOf(pdObj.getString("Reported Location").split("Enclosure")[1].split(",")[0].replace(" ", "")));
                    pd.setDrive(pdObj.getString("Reported Location").split("Slot")[1].split("\\(")[0].replace(" ", ""));
                    pd.setSyncTime(catalogObj.getDate("updatedAt").getTime());
                    disks.add(pd);
                }
            }
            //hp 磁盘
            if ("hpssaraid-controllers".equalsIgnoreCase(catalogObj.getString("source"))) {
                JSONArray diskData = catalogObj.getJSONArray("data");
                for (int k = 0; k < diskData.size(); k++) {
                    JSONObject disk = diskData.getJSONObject(k);
                    Disk pd = new Disk();
                    pd.setEnclosureId(0);
                    //暂时只支持一块raid卡 所以写死为0
                    pd.setControllerId(0);
                    //拼装成可以直接给RackHD shell使用的形式
                    pd.setDrive(disk.getString("Port") + ":" + disk.getString("Box") + ":" + disk.getString("Bay"));
                    pd.setRaid(disk.getString("raid"));
                    pd.setSize(disk.getString("Size"));
                    pd.setType(disk.getString("Interface Type"));
                    pd.setSn(disk.getString("Serial Number"));
                    pd.setModel(disk.getString("Model"));
                    pd.setVirtualDisk(null);
                    pd.setSyncTime(catalogObj.getDate("updatedAt").getTime());
                    disks.add(pd);
                }
            }
            if ("lshw".equalsIgnoreCase(catalogObj.getString("source"))) {
                JSONObject networkData = catalogObj.getJSONObject("data");
                JSONArray childrens = networkData.getJSONArray("children");
                for (int k = 0; k < childrens.size(); k++) {
                    JSONObject children = childrens.getJSONObject(k);
                    if (children.getString("id").equalsIgnoreCase("core")) {
                        JSONArray childs = children.getJSONArray("children");
                        for (int l = 0; l < childs.size(); l++) {
                            JSONObject child = childs.getJSONObject(l);
                            if (child.getString("id").equalsIgnoreCase("pci")) {
                                JSONArray pcis = child.getJSONArray("children");
                                for (int m = 0; m < pcis.size(); m++) {
                                    JSONObject pci = pcis.getJSONObject(m);
                                    if (pci.getString("id").contains("pci") && pci.get("children") != null) {
                                        JSONArray networks = pci.getJSONArray("children");
                                        for (int n = 0; n < networks.size(); n++) {
                                            JSONObject network = networks.getJSONObject(n);
                                            if (network.getString("id").contains("network")) {
                                                NetworkCard pmNetworkCard = new NetworkCard();
                                                if (network.getString("logicalname") != null) {
                                                    pmNetworkCard.setNumber(network.getString("logicalname"));
                                                }
                                                if (network.getString("serial") != null) {
                                                    pmNetworkCard.setMac(network.getString("serial"));
                                                }
                                                if (network.getJSONObject("configuration").getString("ip") != null) {
                                                    pmNetworkCard.setIp(network.getJSONObject("configuration").getString("ip"));
                                                }
                                                pmNetworkCard.setSyncTime(catalogObj.getDate("updatedAt").getTime());
                                                pmNetworkCards.add(pmNetworkCard);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if ("ohai".equalsIgnoreCase(catalogObj.getString("source"))) {
                JSONObject networkData = catalogObj.getJSONObject("data");
                JSONObject networkObj = networkData.getJSONObject("network");
                JSONObject interfaceObj = networkObj.getJSONObject("interfaces");
                Set<Map.Entry<String, Object>> cardsIdSet = interfaceObj.entrySet();
                for (Map.Entry<String, Object> entry : cardsIdSet) {
                    if (entry.getKey().startsWith("e")) {
                        NetworkCard pmNetworkCard = new NetworkCard();
                        pmNetworkCard.setNumber(entry.getKey());
                        JSONObject addressObj = JSONObject.parseObject(((JSONObject) entry.getValue()).getString("addresses"));
                        pmNetworkCard.setMac(addressObj.keySet().stream().filter(s -> s.matches("(\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2})")).findFirst().get());
                        pmNetworkCard.setSyncTime(catalogObj.getDate("updatedAt").getTime());
                        pmNetworkCards.add(pmNetworkCard);
                    }
                }
            }

            if ("smart".equalsIgnoreCase(catalogObj.getString("source"))) {
                JSONArray smartArr = catalogObj.getJSONArray("data");
                for (int k = 0; k < smartArr.size(); k++) {
                    JSONObject smartObj = smartArr.getJSONObject(k);
                    JSONObject smartDisk = smartObj.getJSONObject("SMART");
                    if (smartDisk.containsKey("Identity")) {
                        JSONObject identityObj = smartDisk.getJSONObject("Identity");
                        if (identityObj.containsKey("User Capacity")) {
                            String diskCapacityStr = identityObj.getString("User Capacity").split(" ")[0].replace(",", "");
                            if (identityObj.getString("User Capacity").contains("bytes")) {
                                en.setDisk(Long.parseLong(diskCapacityStr) / 1024 / 1024 / 1024);
                            }
                        }
                    }
                }
            }
        }
        en.setNetworkCards(getLatestCards(pmNetworkCards));
        en.setMemory(memorys);
        en.setMemories(memories);
        en.setCpus(cpus);
        en.setNodeId(nodeId);
        //由于多次使用perccli catalog获取最新的磁盘信息来作为raid创建的依据，因此disk数据可能是磁盘数的倍数 所以需要取得最新的磁盘信息
        if (disks.size() == 0 && needPerccliCatalog(ip, nodeId, en.getBrand()) && !("retry".equals(request))) {
            return getNodeEntity(ip, nodeId, "retry");
        }
        en.setDisks(getLatestDisk(disks));
        return en;
    }

    private List<Disk> getLatestDisk(List<Disk> disks) {
        if (CollectionUtils.isEmpty(disks)) {
            return disks;
        }
        Map<Long, List<Disk>> diskMap = disks.stream().collect(Collectors.groupingBy(Disk::getSyncTime));
        return diskMap.get(diskMap.keySet().stream().max(Long::compareTo).orElse(null));
    }

    private List<NetworkCard> getLatestCards(List<NetworkCard> pmNetworkCards) {
        if (CollectionUtils.isEmpty(pmNetworkCards)) {
            return pmNetworkCards;
        }
        Map<Long, List<NetworkCard>> diskMap = pmNetworkCards.stream().collect(Collectors.groupingBy(NetworkCard::getSyncTime));
        return diskMap.get(diskMap.keySet().stream().max(Long::compareTo).orElse(null));
    }

    public BareMetal convertToPm(MachineEntity machineEntity) throws Exception {
        BareMetal physicalMachine = new BareMetal();
        BeanUtils.copyProperties(physicalMachine, machineEntity);

        physicalMachine.setId(UUIDUtil.newUUID());
        physicalMachine.setMachineModel(machineEntity.getBrand() + " " + machineEntity.getModel());
        physicalMachine.setUpdateTime(System.currentTimeMillis());
        physicalMachine.setStatus(LifeStatus.ready.toString());
        physicalMachine.setManagementIp(machineEntity.getBmcIp());
        physicalMachine.setMachineSn(machineEntity.getSerialNo());
        //nodeId 存到serverId字段
        physicalMachine.setServerId(machineEntity.getNodeId());

        physicalMachine.setEndpointId(machineEntity.getEndPoint());
        physicalMachine.setProviderId("rackhd");
        physicalMachine.setRuleId("rackhd");
        physicalMachine.setMachineBrand(machineEntity.getBrand());
        physicalMachine.setCreateTime(System.currentTimeMillis());
        physicalMachine.setMemory(physicalMachine.getMemory());
        physicalMachine.setDisk(physicalMachine.getDisk());
        physicalMachine.setMemoryType(machineEntity.getMemoryType());
        int diskSize = 0;
        String thisDisk = null;
        if (!org.apache.shiro.util.CollectionUtils.isEmpty(machineEntity.getDisks())) {
            for (Disk d : machineEntity.getDisks()) {
                thisDisk = d.getSize();
                diskSize += Integer.parseInt(DiskUtils.getDiskManufactorValue(thisDisk));
            }
        }

        physicalMachine.setDisk(diskSize);
        if (diskSize == 0)
            physicalMachine.setDisk((int) machineEntity.getDisk());
        physicalMachine.setWorkspaceId("root");
        if (!org.apache.shiro.util.CollectionUtils.isEmpty(machineEntity.getIpArray())) {
            JsonArray jsonArray = new JsonArray();
            for (String element : machineEntity.getIpArray()) {
                jsonArray.add(element);
            }
            physicalMachine.setIpArray(jsonArray.toString());
        }
        //新发现的一律设置为未知 只有填写好带外信息 能定时获取到状态到才有该字段
        physicalMachine.setPower(RackHDConstants.PM_POWER_UNKNOWN);
        return physicalMachine;
    }

    public boolean postWorkflow(String url, String nodeId, String workflow, JSONObject param) {

        RackHDResponse response = RackHDHttpClientUtil.post(String.format(url + "/api/2.0/nodes/%s/workflows?name=" + workflow, nodeId), param == null ? "" : param.toJSONString());
        if (response.getReCode() > RackHDConstants.ERROR_RE_CODE) {
            throw new RuntimeException("操作失败！" + response.getData());
        }
        String workflowId = (JSONObject.parseObject(response.getData())).getString("instanceId");
        try {
            WorkflowResponse workflowResponse = getWorkflowResponse(url, workflowId);
            return workflowResponse.isSuccess();
        } catch (Exception e) {
            throw new RuntimeException("操作失败！" + ExceptionUtils.getExceptionDetail(e));
        }
    }

    public List<String> getActiveWorkflowNodeIds(String endPointId) {
        Endpoint endpoint = endpointMapper.selectByPrimaryKey(endPointId);
        if (endpoint == null) return new ArrayList<>();

        String response = RackHDHttpClientUtil.get(WorkflowConfig.geRackhdUrlById(endPointId) + "/api/2.0/workflows?active=true", null);
        if (StringUtils.isNotBlank(response)) {
            JSONArray workflowArr = JSONArray.parseArray(response);
            if (workflowArr.size() > 0) {
                return workflowArr.stream().map(w -> ((JSONObject) w).getString("node")).filter(s -> StringUtils.isNotBlank(s)).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    public boolean deleteNode(BareMetal bareMetal) {
        RackHDResponse response = RackHDHttpClientUtil.delete(String.format(WorkflowConfig.geRackhdUrlById(bareMetal.getEndpointId()) + "/api/2.0/nodes/%s", bareMetal.getServerId()));
        return response.getReCode() <= RackHDConstants.ERROR_RE_CODE;
    }
}
