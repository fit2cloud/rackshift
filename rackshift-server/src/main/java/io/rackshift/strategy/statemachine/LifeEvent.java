package io.rackshift.strategy.statemachine;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.model.WorkflowRequestDTO;

public class LifeEvent {

    private LifeEventType eventType;
    private WorkflowRequestDTO workflowRequestDTO = new WorkflowRequestDTO();

    public static LifeEvent builder() {
        return new LifeEvent();
    }

    public LifeEvent withBareMetalId(String id) {
        this.getWorkflowRequestDTO().setBareMetalId(id);
        return this;
    }

    public LifeEvent withEventType(LifeEventType type) {
        this.setEventType(type);
        return this;
    }

    public LifeEvent withParams(JSONObject params) {
        this.getWorkflowRequestDTO().setParams(params);
        return this;
    }

    public LifeEventType getEventType() {
        return eventType;
    }

    public void setEventType(LifeEventType eventType) {
        this.eventType = eventType;
    }

    public WorkflowRequestDTO getWorkflowRequestDTO() {
        return workflowRequestDTO;
    }

    public LifeEvent withWorkflowRequestDTO(WorkflowRequestDTO workflowRequestDTO) {
        this.workflowRequestDTO = workflowRequestDTO;
        return this;
    }

    public void setBareMetalId(String id) {
        this.workflowRequestDTO.setBareMetalId(id);
    }

    public String getBareMetalId() {
        return this.workflowRequestDTO.getBareMetalId();
    }

}
