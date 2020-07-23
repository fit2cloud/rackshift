package io.rackshift.state;

import io.rackshift.state.request.BaseRequest;

import java.util.List;

public enum LifeEvent {

    FILL_OBMS("补充OBM信息", null),
    PXE_BOOT_START("PXE启动", null),
    PXE_BOOT_END("PXE启动完毕", null),
    PXE_BOOT_CANCEL("取消PXE启动", null),
    POST_DISCOVERY_WORKFLOW_START("下发发现workflow", null),
    POST_DISCOVERY_WORKFLOW_END("发现workflow执行完毕", null),
    POST_DISCOVERY_WORKFLOW_CANCEL("取消发现worflow", null),
    POST_OTHER_WORKFLOW_START("下发其他workflow", null),
    POST_OTHER_WORKFLOW_END("workflow执行完毕", null),
    POST_OTHER_WORKFLOW_CANCEL("取消workflow", null);
    private String desc;
    private List<String> workflows;
    private BaseRequest request;

    LifeEvent(String desc, List<String> workflows) {
        this.desc = desc;
        this.workflows = workflows;
    }

    void withParams(BaseRequest request) {
        this.request = request;
    }

    public BaseRequest getRequest() {
        return request;
    }

    LifeEvent fromWorkflow(String name) {
        for (LifeEvent event : LifeEvent.values()) {
            if (event.workflows.contains(name)) {
                return event;
            }
        }
        throw new RuntimeException("unsupported workflow!");
    }
}
