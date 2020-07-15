package io.rackshift.plugin.dell.utils;

import java.util.Map;

public class Result {
    private int code;
    private Map<String,String> header;
    private String headerStr;
    private String resultStr;

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public String getHeaderStr() {
        return headerStr;
    }

    public void setHeaderStr(String headerStr) {
        this.headerStr = headerStr;
    }

    public String getResultStr() {
        return resultStr;
    }

    public void setResultStr(String resultStr) {
        this.resultStr = resultStr;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
