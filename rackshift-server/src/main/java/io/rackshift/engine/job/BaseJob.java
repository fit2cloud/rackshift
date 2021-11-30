package io.rackshift.engine.job;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.MqConstants;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.BareMetalMapper;
import io.rackshift.mybatis.mapper.SystemParameterMapper;
import io.rackshift.mybatis.mapper.TaskMapper;
import io.rackshift.mybatis.mapper.WorkflowMapper;
import io.rackshift.service.OutBandService;
import io.rackshift.strategy.statemachine.LifeEvent;
import io.rackshift.strategy.statemachine.LifeEventType;
import io.rackshift.strategy.statemachine.StateMachine;
import io.rackshift.utils.JSONUtils;
import io.rackshift.utils.MqUtil;
import io.rackshift.utils.SpringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

/**
 * graphObject sample
 * <p>
 * {
 * "995872d1-ee6c-4612-9cee-cba092d766b91": {
 * "ignoreFailure": true,
 * "bareMetalId": "cbf26390-d738-47a3-bb76-a78a30c31791",
 * "label": "set-boot-pxe",
 * "taskStartTime": 1631269914016,
 * "implementsTask": "Task.Base.Obm.Node",
 * "instanceId": "995872d1-ee6c-4612-9cee-cba092766b91",
 * "options": {
 * "hostname": "rackshift-node",
 * "networkDevices": [
 * {
 * "ipv4": {
 * "ipAddr": "192.168.1.10",
 * "gateway": "192.168.1.1",
 * "netmask": "255.255.255.0"
 * }
 * }
 * ],
 * "installPartitions": [
 * {
 * "mountPoint": "/",
 * "size": "auto",
 * "fsType": "ext3",
 * "deviceType": "standard"
 * },
 * {
 * "mountPoint": "swap",
 * "size": "4096",
 * "fsType": "swap",
 * "deviceType": "standard"
 * },
 * {
 * "mountPoint": "/boot",
 * "size": "4096",
 * "fsType": "ext3",
 * "deviceType": "standard"
 * },
 * {
 * "mountPoint": "biosboot",
 * "size": "1",
 * "fsType": "biosboot",
 * "deviceType": "standard"
 * }
 * ],
 * "installDisk": "/dev/sda",
 * "action": "setBootPxe",
 * "bonds": [],
 * "version": "7",
 * "rootPassword": "RackShift"
 * },
 * "taskName": "Task.Obm.Node.PxeBoot",
 * "state": "pending",
 * "injectableName": "Task.Obm.Node.PxeBoot",
 * "friendlyName": "Set Node Pxeboot",
 * "properties": {
 * "power": {}
 * },
 * "runJob": "Job.Obm.Node"
 * },
 * "f1f000af-c784-40b5-92ba-d6d527a8d253": {
 * "bareMetalId": "cbf26390-d738-47a3-bb76-a78a30c31791",
 * "label": "reboot",
 * "taskStartTime": 1631269914017,
 * "implementsTask": "Task.Base.Obm.Node",
 * "waitingOn": {
 * "995872d1-ee6c-4612-9cee-cba092766b91": "finished"
 * },
 * "instanceId": "f1f000af-c784-40b5-92ba-d6d527a8d253",
 * "options": {
 * "hostname": "rackshift-node",
 * "networkDevices": [
 * {
 * "ipv4": {
 * "ipAddr": "192.168.1.10",
 * "gateway": "192.168.1.1",
 * "netmask": "255.255.255.0"
 * }
 * }
 * ],
 * "installPartitions": [
 * {
 * "mountPoint": "/",
 * "size": "auto",
 * "fsType": "ext3",
 * "deviceType": "standard"
 * },
 * {
 * "mountPoint": "swap",
 * "size": "4096",
 * "fsType": "swap",
 * "deviceType": "standard"
 * },
 * {
 * "mountPoint": "/boot",
 * "size": "4096",
 * "fsType": "ext3",
 * "deviceType": "standard"
 * },
 * {
 * "mountPoint": "biosboot",
 * "size": "1",
 * "fsType": "biosboot",
 * "deviceType": "standard"
 * }
 * ],
 * "installDisk": "/dev/sda",
 * "action": "reboot",
 * "bonds": [],
 * "version": "7",
 * "rootPassword": "RackShift"
 * },
 * "taskName": "Task.Obm.Node.Reboot",
 * "state": "pending",
 * "injectableName": "Task.Obm.Node.Reboot",
 * "friendlyName": "Reboot Node",
 * "properties": {
 * "power": {
 * "state": "reboot"
 * }
 * },
 * "runJob": "Job.Obm.Node"
 * },
 * "20735714-751b-4e51-8273-a0f5ed10e7a4": {
 * "bareMetalId": "cbf26390-d738-47a3-bb76-a78a30c31791",
 * "label": "install-os",
 * "taskStartTime": 1631269914018,
 * "implementsTask": "Task.Base.Os.Install",
 * "waitingOn": {
 * "f1f000af-c784-40b5-92ba-d6d527a8d253": "succeeded"
 * },
 * "instanceId": "20735714-751b-4e51-8273-a0f5ed10e7a4",
 * "optionsSchema": "install-centos.json",
 * "options": {
 * "networkDevices": [
 * {
 * "ipv4": {
 * "ipAddr": "192.168.1.10",
 * "gateway": "192.168.1.1",
 * "netmask": "255.255.255.0"
 * }
 * }
 * ],
 * "installDisk": "/dev/sda",
 * "comport": "ttyS0",
 * "rackhdCallbackScript": "centos.rackhdcallback",
 * "remoteLogging": false,
 * "profile": "install-centos.ipxe",
 * "installScript": "centos-ks",
 * "bonds": [],
 * "version": "7",
 * "hostname": "rackshift-node",
 * "installPartitions": [
 * {
 * "mountPoint": "/",
 * "size": "auto",
 * "fsType": "ext3",
 * "deviceType": "standard"
 * },
 * {
 * "mountPoint": "swap",
 * "size": "4096",
 * "fsType": "swap",
 * "deviceType": "standard"
 * },
 * {
 * "mountPoint": "/boot",
 * "size": "4096",
 * "fsType": "ext3",
 * "deviceType": "standard"
 * },
 * {
 * "mountPoint": "biosboot",
 * "size": "1",
 * "fsType": "biosboot",
 * "deviceType": "standard"
 * }
 * ],
 * "osType": "linux",
 * "installScriptUri": "{{ api.templates }}/{{ options.installScript }}?nodeId={{ task.nodeId }}",
 * "rootPassword": "RackShift",
 * "progressMilestones": {
 * "requestProfile": {
 * "value": 1,
 * "description": "Enter ipxe and request OS installation profile"
 * },
 * "enterProfile": {
 * "value": 2,
 * "description": "Enter profile, start to download installer"
 * },
 * "startInstaller": {
 * "value": 3,
 * "description": "Start installer and prepare installation"
 * },
 * "preConfig": {
 * "value": 4,
 * "description": "Enter Pre OS configuration"
 * },
 * "postConfig": {
 * "value": 5,
 * "description": "Enter Post OS configuration"
 * },
 * "completed": {
 * "value": 6,
 * "description": "Finished OS installation"
 * }
 * }
 * },
 * "taskName": "Task.Os.Install.CentOS",
 * "state": "pending",
 * "injectableName": "Task.Os.Install.CentOS",
 * "friendlyName": "Install CentOS",
 * "properties": {
 * "os": {
 * "linux": {
 * "distribution": "centos"
 * }
 * }
 * },
 * "runJob": "Job.Os.Install"
 * },
 * "7a4d326c-4cd3-4ed9-b08a-d23e5a692203": {
 * "bareMetalId": "cbf26390-d738-47a3-bb76-a78a30c31791",
 * "label": "rackhd-callback-notification-wait",
 * "taskStartTime": 1631269914018,
 * "implementsTask": "Task.Base.Wait.Notification",
 * "waitingOn": {
 * "20735714-751b-4e51-8273-a0f5ed10e7a4": "succeeded"
 * },
 * "instanceId": "7a4d326c-4cd3-4ed9-b08a-d23e5a692203",
 * "options": {
 * "hostname": "rackshift-node",
 * "networkDevices": [
 * {
 * "ipv4": {
 * "ipAddr": "192.168.1.10",
 * "gateway": "192.168.1.1",
 * "netmask": "255.255.255.0"
 * }
 * }
 * ],
 * "installPartitions": [
 * {
 * "mountPoint": "/",
 * "size": "auto",
 * "fsType": "ext3",
 * "deviceType": "standard"
 * },
 * {
 * "mountPoint": "swap",
 * "size": "4096",
 * "fsType": "swap",
 * "deviceType": "standard"
 * },
 * {
 * "mountPoint": "/boot",
 * "size": "4096",
 * "fsType": "ext3",
 * "deviceType": "standard"
 * },
 * {
 * "mountPoint": "biosboot",
 * "size": "1",
 * "fsType": "biosboot",
 * "deviceType": "standard"
 * }
 * ],
 * "installDisk": "/dev/sda",
 * "bonds": [],
 * "version": "7",
 * "rootPassword": "RackShift"
 * },
 * "taskName": "Task.Wait.Notification",
 * "state": "pending",
 * "injectableName": "Task.Wait.Notification",
 * "friendlyName": "Notification Trigger",
 * "properties": {},
 * "runJob": "Job.Wait.Notification"
 * }
 * }
 */
public abstract class BaseJob {
    protected String bareMetalId;
    protected String taskId;
    protected String instanceId;
    protected JSONObject options;
    protected Map<String, String> renderOptions;
    protected JSONObject context;
    protected String _status;
    protected TaskMapper taskMapper;
    protected TaskWithBLOBs task;
    protected ApplicationContext applicationContext;
    protected Map<String, Class> job;
    protected RabbitTemplate rabbitTemplate;
    protected List<String> finalStateList = new ArrayList<String>() {{
        add(ServiceConstants.TaskStatusEnum.cancelled.name());
        add(ServiceConstants.TaskStatusEnum.failed.name());
        add(ServiceConstants.TaskStatusEnum.succeeded.name());
    }};

    protected Set<String> nextStateSet = new HashSet<String>() {{
        add(ServiceConstants.TaskStatusEnum.finished.name());
        add(ServiceConstants.TaskStatusEnum.succeeded.name());
    }};

    protected BaseJob() {
        this.renderOptions = (Map<String, String>) SpringUtils.getApplicationContext().getBean("renderOptions");
    }

    public void init() {
        Task task = taskMapper.selectByPrimaryKey(taskId);
        this.options.put("nodeId", task.getBareMetalId());
        this.subscribeForDeletion(o -> {
            this.deleteQueue();
            return "ok";
        });
    }

    abstract public void run();

    protected JSONObject getTaskByInstanceId(String instanceId) {
        JSONObject graphObjects = JSONObject.parseObject(task.getGraphObjects());
        return graphObjects.getJSONObject(instanceId);
    }

    protected void setTask(JSONObject task) {
        JSONObject graphObjects = JSONObject.parseObject(this.task.getGraphObjects());
        graphObjects.put(instanceId, task);
        this.task.setGraphObjects(graphObjects.toJSONString());
        taskMapper.updateByPrimaryKeyWithBLOBs(this.task);
    }

    protected void updateAllOptions(JSONObject options) {
        JSONObject graphObjects = JSONObject.parseObject(this.task.getGraphObjects());
        for (String k : graphObjects.keySet()) {
            graphObjects.getJSONObject(k).put("options", JSONUtils.merge(options, graphObjects.getJSONObject(k).getJSONObject("options")));
        }
        this.task.setGraphObjects(graphObjects.toJSONString());
        taskMapper.updateByPrimaryKeyWithBLOBs(this.task);
    }

    public void cancel() {
        if (ServiceConstants.RackHDTaskStatusEnum.pending.name().equalsIgnoreCase(this._status)) {
            JSONObject task = getTaskByInstanceId(instanceId);
            task.put("state", ServiceConstants.RackHDTaskStatusEnum.cancelled.name());
            this._status = ServiceConstants.RackHDTaskStatusEnum.cancelled.name();
            setTask(task);
        }
    }

    public void succeeded() {
        if (ServiceConstants.RackHDTaskStatusEnum.pending.name().equalsIgnoreCase(this._status)) {
            JSONObject task = getTaskByInstanceId(instanceId);
            task.put("state", ServiceConstants.RackHDTaskStatusEnum.succeeded.name());
            this._status = ServiceConstants.RackHDTaskStatusEnum.succeeded.name();
            setTask(task);
            deleteQueue();
        }
        nextTask(ServiceConstants.RackHDTaskStatusEnum.succeeded.name());
    }


    public void nextTask(String status) {
        nextStateSet.add(status);
        JSONObject graphObjects = JSONObject.parseObject(this.task.getGraphObjects());
        boolean go = false;
        //没有 waitingOn 属性的任务，可能有多个需要分别处理
        List<String> firstTaskList = new ArrayList<>();

        for (String s : graphObjects.keySet()) {
            JSONObject waitingOnObj = graphObjects.getJSONObject(s).getJSONObject("waitingOn");
            if (waitingOnObj != null) {
                for (String waitInstance : waitingOnObj.keySet()) {
                    if (waitInstance.equalsIgnoreCase(this.instanceId) && nextStateSet.contains(waitingOnObj.getString(waitInstance))) {
                        JSONObject body = new JSONObject();
                        body.put("taskId", taskId);
                        body.put("instanceId", graphObjects.getJSONObject(s).getString("instanceId"));
                        Message message = new Message(body.toJSONString().getBytes(StandardCharsets.UTF_8));
                        rabbitTemplate.send(MqConstants.RUN_TASK_QUEUE_NAME, message);
                        go = true;
                        break;
                    }
                }
            } else {
                firstTaskList.add(s);
            }
        }
        if (!go) {
            JSONObject taskObj = JSONObject.parseObject(task.getGraphObjects());
            List<JSONObject> tasksReadyToStart = new ArrayList<>();
            for (String t : taskObj.keySet()) {
                if (taskObj.getJSONObject(t).getJSONObject("waitingOn") == null && taskObj.getJSONObject(t).getString("state").equalsIgnoreCase("pending")) {
                    tasksReadyToStart.add(taskObj.getJSONObject(t));
                }
            }
            //假如有多个启动任务只启动一个
            for (JSONObject jsonObject : tasksReadyToStart) {
                JSONObject body = new JSONObject();
                body.put("taskId", task.getId());
                body.put("instanceId", jsonObject.getString("instanceId"));
                Message message = new Message(body.toJSONString().getBytes(StandardCharsets.UTF_8));
                rabbitTemplate.send(MqConstants.RUN_TASK_QUEUE_NAME, message);
                go = true;
                break;
            }
        }

        //只有一条任务流的时候 或者多个起始任务完成 主 taskgrah 直接结束
        if (!go) {
            if (firstTaskList.size() == 1 || isEnd(firstTaskList, graphObjects)) {
                JSONObject task = getTaskByInstanceId(instanceId);
                task.put("state", ServiceConstants.RackHDTaskStatusEnum.succeeded.name());
                this._status = ServiceConstants.RackHDTaskStatusEnum.succeeded.name();
                this.task.setStatus(ServiceConstants.TaskStatusEnum.succeeded.name());
                setTask(task);
                JSONObject result = new JSONObject();
                result.put("result", true);
                Workflow w = applicationContext.getBean(WorkflowMapper.class).selectByPrimaryKey(this.task.getWorkFlowId());
                //
                if (w == null) return;
                if (w.getInjectableName().contains("Graph.Install")) {
                    sendBMLifecycleEvent(LifeEventType.POST_OS_WORKFLOW_END, result);
                } else if (w.getInjectableName().equalsIgnoreCase("Graph.rancherDiscovery")) {
                    sendBMLifecycleEvent(LifeEventType.POST_DISCOVERY_WORKFLOW_END, result);
                    generateOutband();
                } else {
                    sendBMLifecycleEvent(LifeEventType.POST_OTHER_WORKFLOW_END, result);
                }
            }
        }
    }

    private void generateOutband() {
        SystemParameter createCredential = applicationContext.getBean(SystemParameterMapper.class).selectByPrimaryKey("bmc_credentials");
        String userName = Optional.ofNullable(applicationContext.getBean(SystemParameterMapper.class).selectByPrimaryKey("bmc_username")).orElse(new SystemParameter()).getParamValue();
        String password = Optional.ofNullable(applicationContext.getBean(SystemParameterMapper.class).selectByPrimaryKey("bmc_password")).orElse(new SystemParameter()).getParamValue();
        if (createCredential != null && Boolean.TRUE.toString().equalsIgnoreCase(createCredential.getParamValue())) {
            OutBand outBand = new OutBand();
            outBand.setIp(Optional.ofNullable(applicationContext.getBean(BareMetalMapper.class).selectByPrimaryKey(bareMetalId)).orElse(new BareMetal()).getManagementIp());
            outBand.setUserName(Optional.ofNullable(userName).orElse("rackshift"));
            outBand.setPwd(Optional.ofNullable(password).orElse("rackshift"));
            outBand.setBareMetalId(bareMetalId);
            applicationContext.getBean(OutBandService.class).saveOrUpdate(outBand, false);
        }
    }

    private boolean isEnd(List<String> firstTaskList, JSONObject graphObjects) {
        for (String s : firstTaskList) {
            if (!finalStateList.contains(graphObjects.getJSONObject(s).getString("state"))) {
                return false;
            }
        }
        return true;
    }

    public void error(Exception e) {
        if (ServiceConstants.RackHDTaskStatusEnum.pending.name().equalsIgnoreCase(this._status)) {
            JSONObject task = getTaskByInstanceId(instanceId);
            task.put("state", ServiceConstants.RackHDTaskStatusEnum.failed.name());
            task.put("error", e.getMessage());
            this._status = ServiceConstants.RackHDTaskStatusEnum.failed.name();
            //compatible with ignoreFailure
            if (context.containsKey("ignoreFailure") || context.getBooleanValue("ignoreFailure")) {
                setTask(task);
                nextTask(ServiceConstants.RackHDTaskStatusEnum.finished.name());
            } else {
                this.task.setStatus(ServiceConstants.TaskStatusEnum.failed.name());
                deleteQueue();
                JSONObject result = new JSONObject();
                result.put("result", false);
                sendBMLifecycleEvent(LifeEventType.POST_OTHER_WORKFLOW_END, result);
                setTask(task);
            }
        }
    }

    public void complete() {
        if (ServiceConstants.RackHDTaskStatusEnum.pending.name().equalsIgnoreCase(this._status)) {
            JSONObject task = getTaskByInstanceId(instanceId);
            task.put("state", ServiceConstants.RackHDTaskStatusEnum.succeeded.name());
            this._status = ServiceConstants.RackHDTaskStatusEnum.succeeded.name();
            setTask(task);
            deleteQueue();
        }
        nextTask(ServiceConstants.RackHDTaskStatusEnum.finished.name());
    }

    public void completeNoQueue() {
        if (ServiceConstants.RackHDTaskStatusEnum.pending.name().equalsIgnoreCase(this._status)) {
            JSONObject task = getTaskByInstanceId(instanceId);
            task.put("state", ServiceConstants.RackHDTaskStatusEnum.succeeded.name());
            this._status = ServiceConstants.RackHDTaskStatusEnum.succeeded.name();
            setTask(task);
        }
        nextTask(ServiceConstants.RackHDTaskStatusEnum.finished.name());
    }


    protected void subscribeForRequestCommand(Function callback) {
        MqUtil.subscribe(MqConstants.EXCHANGE_NAME, MqConstants.MQ_ROUTINGKEY_COMMANDS + this.bareMetalId, callback);
    }

    protected void subscribeForRequestProfile(Function callback) {
        MqUtil.subscribe(MqConstants.EXCHANGE_NAME, MqConstants.MQ_ROUTINGKEY_PROFILES + this.bareMetalId, callback);
    }

    protected void subscribeForRequestOptions(Function callback) {
        MqUtil.subscribe(MqConstants.EXCHANGE_NAME, MqConstants.MQ_ROUTINGKEY_OPTIONS + this.bareMetalId, callback);
    }

    protected void subscribeForCompleteCommands(Function callback) {
        MqUtil.subscribe(MqConstants.EXCHANGE_NAME, MqConstants.MQ_ROUTINGKEY_COMPLETE + this.bareMetalId, callback);
    }

    protected void subscribeForNotification(Function callback) {
        MqUtil.subscribe(MqConstants.EXCHANGE_NAME, MqConstants.MQ_ROUTINGKEY_NOTIFICATION + this.bareMetalId, callback);
    }

    protected void subscribeForDeletion(Function callback) {
        MqUtil.subscribe(MqConstants.EXCHANGE_NAME, MqConstants.MQ_ROUTINGKEY_DELETION + this.bareMetalId, callback);
    }

    private void deleteQueue() {
        MqUtil.delQueue(MqConstants.EXCHANGE_NAME, MqConstants.MQ_ROUTINGKEY_COMMANDS + this.bareMetalId);
        MqUtil.delQueue(MqConstants.EXCHANGE_NAME, MqConstants.MQ_ROUTINGKEY_PROFILES + this.bareMetalId);
        MqUtil.delQueue(MqConstants.EXCHANGE_NAME, MqConstants.MQ_ROUTINGKEY_OPTIONS + this.bareMetalId);
        MqUtil.delQueue(MqConstants.EXCHANGE_NAME, MqConstants.MQ_ROUTINGKEY_COMPLETE + this.bareMetalId);
        MqUtil.delQueue(MqConstants.EXCHANGE_NAME, MqConstants.MQ_ROUTINGKEY_NOTIFICATION + this.bareMetalId);
        MqUtil.delQueue(MqConstants.EXCHANGE_NAME, MqConstants.MQ_ROUTINGKEY_DELETION + this.bareMetalId);
    }

    protected void sendBMLifecycleEvent(LifeEventType lifeEventType) {
        WorkflowRequestDTO r = new WorkflowRequestDTO();
        r.setBareMetalId(bareMetalId);
        r.setTaskId(taskId);
        extracted(lifeEventType, r);
    }

    private void extracted(LifeEventType lifeEventType, WorkflowRequestDTO r) {
        LifeEvent event = LifeEvent.builder().withEventType(lifeEventType).withWorkflowRequestDTO(r);
        StateMachine stateMachine = (StateMachine) applicationContext.getBean("stateMachine");
        stateMachine.sendEvent(event);
    }

    protected void sendBMLifecycleEvent(LifeEventType lifeEventType, JSONObject params) {
        WorkflowRequestDTO r = new WorkflowRequestDTO();
        r.setBareMetalId(bareMetalId);
        r.setTaskId(taskId);
        r.setParams(params);
        extracted(lifeEventType, r);
    }

}
