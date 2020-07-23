package io.rackshift.state.request;

public class BaseRequest {
    private String bareMetalId;
    private Object params;

    public BaseRequest(String bareMetalId, Object params) {
        this.bareMetalId = bareMetalId;
        this.params = params;
    }
}
