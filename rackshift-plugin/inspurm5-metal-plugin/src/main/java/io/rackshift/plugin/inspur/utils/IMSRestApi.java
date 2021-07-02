package io.rackshift.plugin.inspur.utils;

import com.google.gson.Gson;
import io.rackshift.metal.sdk.model.MachineEntity;
import io.rackshift.metal.sdk.model.Metric;
import io.rackshift.metal.sdk.model.PluginResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface IMSRestApi {
    boolean login(String ip, String userName, String password);

    MachineEntity getMachineEntity(String ip, String userName, String password);

    Metric getMetric(String ip, String userName, String password);

    Float getPowerMetric(String ip, String userName, String password);

    ConcurrentHashMap<String, Map> headerMap = new ConcurrentHashMap();
    Gson gson = new Gson();

    PluginResult logout(String ip, String userName, String password);
}
