package io.rackshift.model;

/**
 * Created by zk.wang on 2019/6/10.
 */
public class WorkflowResponse {

    private boolean end;
    private boolean success;
    private String message;

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
