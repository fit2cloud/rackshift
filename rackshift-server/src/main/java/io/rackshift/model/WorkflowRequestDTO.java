package io.rackshift.model;

import com.alibaba.fastjson.JSONObject;

public class WorkflowRequestDTO {
    private String bareMetalId;
    private String workflowName;
    private JSONObject params;

    public WorkflowRequestDTO() {

    }

    public WorkflowRequestDTO(String bareMetalId, String workflowName, JSONObject params) {
        this.bareMetalId = bareMetalId;
        this.workflowName = workflowName;
        this.params = params;
    }

    public String getBareMetalId() {
        return bareMetalId;
    }

    public void setBareMetalId(String bareMetalId) {
        this.bareMetalId = bareMetalId;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }
}
