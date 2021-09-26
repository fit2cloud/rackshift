package io.rackshift.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.model.RSException;

import java.util.Map;

public class JSONUtils {
    public static JSONObject merge(Map fromObj, Map toObj) {
        if (fromObj == null || toObj == null) {
            RSException.throwExceptions("merge jsonobject failed ! null obj!");
        }
        fromObj.keySet().forEach(k -> {
            toObj.put(k, fromObj.get(k));
        });
        return (JSONObject) JSONObject.toJSON(toObj);
    }
}
