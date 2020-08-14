package io.rackshift.strategy.statemachine;

import io.rackshift.model.WorkflowRequestDTO;

import java.util.ArrayList;
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
    POST_OTHER_WORKFLOW_CANCEL("取消workflow", null),
    POST_OS_WORKFLOW_START("下发安装系统workflow", new ArrayList<String>() {{
        add("Graph.InstallCentOS");
    }}),
    POST_OS_WORKFLOW_END("安装系统workflow执行完毕", null),
    POST_OS_WORKFLOW_CANCEL("取消安装系统workflow", null);
    private String desc;
    private List<String> workflows;
    private WorkflowRequestDTO workflowRequestDTO = new WorkflowRequestDTO();

    public void setBareMetalId(String id) {
        this.workflowRequestDTO.setBareMetalId(id);
    }

    public String getBareMetalId() {
        return this.workflowRequestDTO.getBareMetalId();
    }

    public String getDesc() {
        return desc;
    }

    LifeEvent(String desc, List<String> workflows) {
        this.desc = desc;
        this.workflows = workflows;
    }

    public WorkflowRequestDTO getWorkflowRequestDTO() {
        return workflowRequestDTO;
    }

    public void setWorkflowRequestDTO(WorkflowRequestDTO workflowRequestDTO) {
        this.workflowRequestDTO = workflowRequestDTO;
    }

    public static LifeEvent fromWorkflow(String name) {
        for (LifeEvent event : LifeEvent.values()) {
            if (event.workflows != null && event.workflows.contains(name)) {
                return event;
            }
        }
        throw new RuntimeException("unsupported workflow!");
    }
}
