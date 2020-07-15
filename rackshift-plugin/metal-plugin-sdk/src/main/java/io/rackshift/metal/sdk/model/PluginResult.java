package io.rackshift.metal.sdk.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 组合返回结果
 */
public class PluginResult {

    public PluginResult() {
        this.success = true;
    }

    public PluginResult(Object data) {
        this.data = data;
        this.success = true;
    }

    private PluginResult(boolean success, String msg) {
        this.success = success;
        this.message = msg;
    }

    private PluginResult(boolean success, String msg, Object data) {
        if (!success) {
//            LogUtil.error(msg);
        }
        this.success = success;
        this.message = msg;
        this.data = data;
    }

    // 请求是否成功
    private boolean success = false;
    // 描述信息
    private String message;
    // 返回数据
    private Object data = "";

    public boolean isSuccess() {
        return this.success;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static PluginResult success() {
        return new PluginResult();
    }

    public static PluginResult success(Object obj) {
        return new PluginResult(obj);
    }

    public static PluginResult success(Object obj, String message) {
        return new PluginResult(true, message, obj);
    }

    public static PluginResult error(String message) {
        return new PluginResult(false, message, null);
    }

    public static PluginResult error(String message, Object object) {
        return new PluginResult(false, message, object);
    }

    public static PluginResult success(String message, Object object) {
        return new PluginResult(true, message, object);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
