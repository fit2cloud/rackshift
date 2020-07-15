package io.rackshift.plugin.dell.utils;

import io.rackshift.metal.sdk.model.MachineEntity;

public interface IIDRACRestAPI {
    boolean login(String ip, String userName, String password);

    boolean logout(String ip);

    MachineEntity getMachineEntity(String ip, String userName, String password);

    Integer getPowerMetric(String ip, String userName, String password);

    int getHwRetryTimes = 10;
}
