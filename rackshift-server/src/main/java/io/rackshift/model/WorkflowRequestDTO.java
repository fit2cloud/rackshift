package io.rackshift.model;

import com.alibaba.fastjson.JSONObject;

public class WorkflowRequestDTO {
    private String bareMetalId;
    private String taskId;
    private String workflowName;
    private String workflowId;
    private JSONObject params;
    private JSONObject extraParams;

    public WorkflowRequestDTO() {

    }

    public WorkflowRequestDTO(String bareMetalId, String workflowName, JSONObject params, JSONObject extraParams) {
        this.bareMetalId = bareMetalId;
        this.workflowName = workflowName;
        this.params = params;
        this.extraParams = extraParams;
    }

    public WorkflowRequestDTO(String bareMetalId, String workflowName, JSONObject params) {
        this.bareMetalId = bareMetalId;
        this.workflowName = workflowName;
        this.params = params;
    }

    public JSONObject getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(JSONObject extraParams) {
        this.extraParams = extraParams;
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }
}
