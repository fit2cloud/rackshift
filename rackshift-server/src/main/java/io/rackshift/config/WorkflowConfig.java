package io.rackshift.config;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import io.rackshift.constants.RackHDConstants;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.engine.basetask.BaseTask;
import io.rackshift.engine.taskgraph.BaseTaskGraph;
import io.rackshift.engine.taskobject.BaseTaskObject;
import io.rackshift.metal.sdk.util.HttpFutureUtils;
import io.rackshift.metal.sdk.util.LogUtil;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.EndpointMapper;
import io.rackshift.mybatis.mapper.WorkflowMapper;
import io.rackshift.service.WorkflowService;
import io.rackshift.strategy.statemachine.LifeEventType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class WorkflowConfig {
    @Resource
    private WorkflowMapper workflowMapper;
    @Resource
    private WorkflowService workflowService;
    @Resource
    private EndpointMapper endpointMapper;
    private String rackhdUrl;
    private List<Endpoint> endPoints;
    static EndpointMapper staticEndpointMapper;

    @Resource
    private void setEndpointMapper(EndpointMapper endpointMapper) {
        staticEndpointMapper = endpointMapper;
    }

    @PostConstruct
    public void initWorkflow() {
        Map<String, List<Workflow>> typeMap = workflowMapper.selectByExample(new WorkflowExample()).stream().collect(Collectors.groupingBy(Workflow::getEventType));
        if (typeMap != null && typeMap.keySet().size() > 0) {
            for (Map.Entry<String, List<Workflow>> wfEntry : typeMap.entrySet()) {
                if (StringUtils.isBlank(wfEntry.getKey())) continue;
                LifeEventType.valueOf(wfEntry.getKey()).addWorkflow(wfEntry.getValue().stream().map(Workflow::getInjectableName).collect(Collectors.toList()));
            }
        }

        EndpointExample e = new EndpointExample();
        e.createCriteria().andTypeEqualTo(ServiceConstants.EndPointType.main_endpoint.name());
        endPoints = endpointMapper.selectByExample(e);
        if (endPoints.size() > 0 && !endPoints.get(0).getIp().contains(":"))
            rackhdUrl = "http://" + endPoints.get(0).getIp() + ":9090";
        else if (endPoints.size() > 0 && endPoints.get(0).getIp().contains(":"))
            rackhdUrl = "http://" + endPoints.get(0).getIp();
        else
            rackhdUrl = "";

        e.clear();
        endPoints = endpointMapper.selectByExample(e);
        endPoints = endPoints.stream().map(endpoint -> {
            if ("rackshift-proxy".equalsIgnoreCase(endpoint.getIp())) {
                try {
                    endpoint.setIp(getGateWayAddress(""));
                } catch (Exception unknownHostException) {
                    unknownHostException.printStackTrace();
                }
            }
            return endpoint;
        }).collect(Collectors.toList());


        Set<Map.Entry<String, BaseTaskGraph>> baseTasks = taskGraph().entrySet();
        baseTasks.forEach(baseTaskGraph -> {
            WorkflowWithBLOBs w = workflowService.getByInjectableName(baseTaskGraph.getValue().getInjectableName());
            if (w != null) {
                if (StringUtils.isBlank(w.getTasks())) {
                    w.setTasks(baseTaskGraph.getValue().getTasks().toJSONString());
                }
                if (StringUtils.isBlank(w.getOptions()) && baseTaskGraph.getValue().getOptions() != null) {
                    w.setOptions(baseTaskGraph.getValue().getOptions().toJSONString());
                }
                workflowService.update(w);
            }
        });
    }

    @Bean
    public Map<String, BaseTask> baseTask() {
        try {
            File[] files = new File(BaseTaskObject.class.getClassLoader().getResource("io/rackshift/engine/basetask").getFile()).listFiles();
            List<BaseTask> objs = new LinkedList<>();
            for (File f : files) {
                if (f.getName().indexOf(".js") == -1)
                    continue;
                BufferedReader reader = new BufferedReader(new FileReader(f));
                String line = null;
                StringBuffer sb = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                ScriptEngineManager manager = new ScriptEngineManager();
                ScriptEngine engine = manager.getEngineByName("Nashorn");
                String a = sb.toString().replace("module.exports =", "var  a =") + "function json(){ return (JSON.stringify(a));}";
                engine.eval(a);
                Invocable invocable = (Invocable) engine;
                String r = (String) invocable.invokeFunction("json", "");
                objs.add(new Gson().fromJson(r, BaseTask.class));
            }
            return objs.stream().collect(Collectors.toMap(BaseTask::getInjectableName, c -> c));
        } catch (Exception e) {
            LogUtil.error("初始化 " + BaseTask.class + " 失败！", e);
        }

        return null;
    }

    @Bean
    public Map<String, BaseTaskGraph> taskGraph() {
        try {
            File[] files = new File(BaseTaskObject.class.getClassLoader().getResource("io/rackshift/engine/taskgraph").getFile()).listFiles();
            List<BaseTaskGraph> objs = new LinkedList<>();
            for (File f : files) {
                if (f.getName().indexOf(".js") == -1)
                    continue;
                BufferedReader reader = new BufferedReader(new FileReader(f));
                String line = null;
                StringBuffer sb = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                ScriptEngineManager manager = new ScriptEngineManager();
                ScriptEngine engine = manager.getEngineByName("Nashorn");
                String a = sb.toString().replace("module.exports =", "var  a =") + "function json(){ return (JSON.stringify(a));}";
                engine.eval(a);
                Invocable invocable = (Invocable) engine;
                String r = (String) invocable.invokeFunction("json", "");
                objs.add(new Gson().fromJson(r, BaseTaskGraph.class));
            }
            return objs.stream().collect(Collectors.toMap(BaseTaskGraph::getInjectableName, c -> c));
        } catch (Exception e) {
            LogUtil.error("初始化 " + BaseTaskGraph.class + " 失败！", e);
        }

        return null;
    }

    @Bean
    public Map<String, BaseTaskObject> taskObject() {
        try {
            File[] files = new File(BaseTaskObject.class.getClassLoader().getResource("io/rackshift/engine/taskobject").getFile()).listFiles();
            List<BaseTaskObject> objs = new LinkedList<>();
            for (File f : files) {
                if (f.getName().indexOf(".js") == -1)
                    continue;
                BufferedReader reader = new BufferedReader(new FileReader(f));
                String line = null;
                StringBuffer sb = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                ScriptEngineManager manager = new ScriptEngineManager();
                ScriptEngine engine = manager.getEngineByName("Nashorn");
                String a = sb.toString().replace("module.exports =", "var  a =") + "function json(){ return (JSON.stringify(a));}";
                engine.eval(a);
                Invocable invocable = (Invocable) engine;
                String r = (String) invocable.invokeFunction("json", "");
                objs.add(new Gson().fromJson(r, BaseTaskObject.class));
            }
            return objs.stream().collect(Collectors.toMap(BaseTaskObject::getInjectableName, c -> c));
        } catch (Exception e) {
            LogUtil.error("初始化 " + BaseTaskObject.class + " 失败！", e);
        }

        return null;
    }

    private static String getGateWayAddress(String ip) throws UnknownHostException, SocketException {
        String localhost = Inet4Address.getLocalHost().getHostAddress();
        if (StringUtils.isNotBlank(ip)) {
            localhost = ip;
        }
        int maskLength = NetworkInterface.getByInetAddress(Inet4Address.getLocalHost()).getInterfaceAddresses().get(0).getNetworkPrefixLength();
        StringBuffer sb = new StringBuffer();
        StringBuffer number = new StringBuffer();

        int i = 0;
        while (i < 32) {
            if (i < maskLength) {
                sb.append("1");
            }
            if ((i + 1) % 8 == 0) {
                number.append(Integer.valueOf(sb.toString().equals("") ? "0" : sb.toString()));
                sb = new StringBuffer();
                if (i != 31) {
                    number.append(".");
                }
            }
            i++;
        }

        String[] s1 = localhost.split("\\.");

        String[] s2 = number.toString().split("\\.");

        for (i = 0; i < s1.length; i++) {
            s1[i] = String.valueOf(Integer.valueOf(s1[i]) & Integer.valueOf(s2[i], 2));
        }
        s1[s1.length - 1] = "1";
        return StringUtils.join(s1, ".");
    }

    public static String geRackhdUrlById(String id) {
        Endpoint endpoint = staticEndpointMapper.selectByPrimaryKey(id);

        if ("rackshift-proxy".equalsIgnoreCase(endpoint.getIp())) {
            try {
                endpoint.setIp(getGateWayAddress(""));
            } catch (Exception unknownHostException) {
                unknownHostException.printStackTrace();
            }
        }

        if (endpoint != null && !endpoint.getIp().contains(":"))
            return "http://" + endpoint.getIp() + ":9090";
        if (endpoint == null)
            return null;

        return "http://" + endpoint.getIp();
    }

    public String getRackhdUrl() {
        return rackhdUrl;
    }

    public List<Endpoint> getEndPoints() {
        return endPoints;
    }

    @Bean
    public JSONArray allWorkflow() {
        try {
            String res = HttpFutureUtils.getHttp(getRackhdUrl() + RackHDConstants.WORKFLOWS, null);
            if (StringUtils.isNotBlank(res))
                return JSONArray.parseArray(res);
        } catch (Exception e) {
        }
        return new JSONArray();
    }

    @Bean
    public JSONArray allTask() {
        try {
            String res = HttpFutureUtils.getHttp(getRackhdUrl() + RackHDConstants.TASKS, null);
            if (StringUtils.isNotBlank(res))
                return JSONArray.parseArray(res);
            return JSONArray.parseArray(res);
        } catch (Exception e) {
        }
        return new JSONArray();
    }
}
