package io.rackshift.engine.job;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.MqConstants;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.mybatis.domain.TaskWithBLOBs;
import io.rackshift.mybatis.mapper.TaskMapper;
import io.rackshift.utils.MqUtil;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Map;
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
    protected JSONObject context;
    protected String _status;
    protected TaskMapper taskMapper;
    protected TaskWithBLOBs task;
    protected ApplicationContext applicationContext;
    protected Map<String, Class> job;
    protected RabbitTemplate rabbitTemplate;

    protected BaseJob() {

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
        }
        nextTick();
    }

    public void nextTick() {
        JSONObject graphObjects = JSONObject.parseObject(this.task.getGraphObjects());
        for (String s : graphObjects.keySet()) {
            JSONObject waitingOnObj = graphObjects.getJSONObject(s).getJSONObject("waitingOn");
            if (waitingOnObj != null && waitingOnObj.getString(waitingOnObj.keySet().stream().findFirst().get()).equalsIgnoreCase(this.instanceId) && waitingOnObj.getString(this.instanceId).equalsIgnoreCase(this._status)) {
                JSONObject body = new JSONObject();
                body.put("taskId", taskId);
                body.put("instanceId", graphObjects.getJSONObject(s).getString("instanceId"));
                Message message = new Message("".getBytes(StandardCharsets.UTF_8));
                rabbitTemplate.send(MqConstants.RUN_TASK_QUEUE_NAME, message);
            }
        }
    }

    public void error(Exception e) {
        if (ServiceConstants.RackHDTaskStatusEnum.pending.name().equalsIgnoreCase(this._status)) {
            JSONObject task = getTaskByInstanceId(instanceId);
            task.put("state", ServiceConstants.RackHDTaskStatusEnum.failed.name());
            task.put("error", e.getMessage());
            this._status = ServiceConstants.RackHDTaskStatusEnum.failed.name();
            this.task.setStatus(ServiceConstants.TaskStatusEnum.failed.name());
            setTask(task);
        }
        complete();
    }

    public void complete() {
        this._status = ServiceConstants.RackHDTaskStatusEnum.finished.name();
        deleteQueue();
        nextTick();
    }


    protected void subscribeForRequestCommand(Function callback) {
        MqUtil.subscribe(MqConstants.EXCHANGE_NAME, "methods.requestCommands." + this.bareMetalId, callback);
    }

    protected void subscribeForRequestProfile(Function callback) {
        MqUtil.subscribe(MqConstants.EXCHANGE_NAME, "methods.requestProfile." + this.bareMetalId, callback);
    }

    protected void subscribeForRequestOptions(Function callback) {
        MqUtil.subscribe(MqConstants.EXCHANGE_NAME, "methods.requestOptions." + this.bareMetalId, callback);
    }

    protected void subscribeForCompleteCommands(Function callback) {
        MqUtil.subscribe(MqConstants.EXCHANGE_NAME, "methods.completeCommands." + this.bareMetalId, callback);
    }

    private void deleteQueue() {
        MqUtil.delQueue(MqConstants.EXCHANGE_NAME, "methods.requestCommands." + this.bareMetalId);
        MqUtil.delQueue(MqConstants.EXCHANGE_NAME, "methods.requestProfile." + this.bareMetalId);
        MqUtil.delQueue(MqConstants.EXCHANGE_NAME, "methods.requestOptions." + this.bareMetalId);
        MqUtil.delQueue(MqConstants.EXCHANGE_NAME, "methods.completeCommands." + this.bareMetalId);
    }
}
