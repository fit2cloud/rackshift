package io.rackshift.model;

import io.rackshift.mybatis.domain.TaskWithBLOBs;

public class TaskDTO extends TaskWithBLOBs {

    private String searchKey;
    private String machineSn;
    private String machineModel;
    private String friendlyName;
    private String sort;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getSort() {
        return sort;
    }

    public String getMachineModel() {
        return machineModel;
    }

    public void setMachineModel(String machineModel) {
        this.machineModel = machineModel;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getMachineSn() {
        return machineSn;
    }

    public void setMachineSn(String machineSn) {
        this.machineSn = machineSn;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }


}
