package io.rackshift.model;

/**
 * Created by zk.wang on 2019/6/10.
 */
public class RackHDResponse {

    private int reCode;
    private String data;
    private String message;

    public int getReCode() {
        return reCode;
    }

    public void setReCode(int reCode) {
        this.reCode = reCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
