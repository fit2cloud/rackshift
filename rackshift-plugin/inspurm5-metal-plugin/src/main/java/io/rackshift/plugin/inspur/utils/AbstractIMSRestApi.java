package io.rackshift.plugin.inspur.utils;

abstract class AbstractIMSRestApi implements IMSRestApi {
    protected static String getResponseJSONString(String a) {
        String temp[] = a.split("\n");
        StringBuffer stringBuffer = new StringBuffer();
        for (String s : temp) {
            if (s.startsWith("//")) continue;
            stringBuffer.append(s);
        }
        return stringBuffer.toString().replace(";", "").split("=")[1];
    }
}
