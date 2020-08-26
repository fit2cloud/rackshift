package io.rackshift.strategy.statemachine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public enum LifeEventType {

    FILL_OBMS("补充OBM信息"),
    PXE_BOOT_START("PXE启动"),
    PXE_BOOT_END("PXE启动完毕"),
    PXE_BOOT_CANCEL("取消PXE启动"),
    POST_DISCOVERY_WORKFLOW_START("下发发现workflow"),
    POST_DISCOVERY_WORKFLOW_END("发现workflow执行完毕"),
    POST_DISCOVERY_WORKFLOW_CANCEL("取消发现worflow"),
    POST_OTHER_WORKFLOW_START("下发其他workflow"),
    POST_OTHER_WORKFLOW_END("workflow执行完毕"),
    POST_OTHER_WORKFLOW_CANCEL("取消workflow"),
    POST_OS_WORKFLOW_START("下发安装系统workflow", new ArrayList<String>() {{
        add("Graph.InstallCentOS");
    }}),
    POST_OS_WORKFLOW_END("安装系统workflow执行完毕", null),
    POST_OS_WORKFLOW_CANCEL("取消安装系统workflow", null);
    private String desc;
    private List<String> workflows = new LinkedList<>();

    public String getDesc() {
        return desc;
    }

    public void addWorkflow(List<String> injectableLst) {
        workflows.addAll(injectableLst);
    }

    LifeEventType(String desc) {
        this.desc = desc;
    }

    LifeEventType(String desc, List<String> workflows) {
        this.desc = desc;
        this.workflows = workflows;
    }

    public static LifeEventType fromWorkflow(String name) {
        for (LifeEventType event : LifeEventType.values()) {
            if (event.workflows != null && event.workflows.contains(name)) {
                return event;
            }
        }
        throw new RuntimeException("unsupported workflow!");
    }

}
