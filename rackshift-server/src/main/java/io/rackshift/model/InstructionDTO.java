package io.rackshift.model;

import io.rackshift.mybatis.domain.Instruction;

public class InstructionDTO extends Instruction {

    private String searchKey;
    private String sort;

    private String machineSn;
    private String machineModel;

    private String[] bareMetalIds;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String[] getBareMetalIds() {
        return bareMetalIds;
    }

    public void setBareMetalIds(String[] bareMetalIds) {
        this.bareMetalIds = bareMetalIds;
    }

    public String getMachineSn() {
        return machineSn;
    }

    public void setMachineSn(String machineSn) {
        this.machineSn = machineSn;
    }

    public String getMachineModel() {
        return machineModel;
    }

    public void setMachineModel(String machineModel) {
        this.machineModel = machineModel;
    }
}
