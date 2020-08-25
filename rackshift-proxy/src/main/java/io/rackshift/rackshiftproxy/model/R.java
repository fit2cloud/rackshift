package io.rackshift.rackshiftproxy.model;

public class R {
    private Object data;
    private boolean success;
    private String msg;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public R(Object data) {
        this.data = data;
        this.success = true;
    }

    public R(Object data, String msg) {
        this.data = data;
        this.success = false;
        this.msg = msg;
    }

    public static R successWithData(Object data) {
        return new R(data);
    }

    public static R failWithMsg(String msg) {
        return new R(null, msg);
    }
}
