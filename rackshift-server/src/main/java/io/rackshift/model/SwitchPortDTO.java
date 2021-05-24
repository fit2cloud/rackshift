package io.rackshift.model;

import io.rackshift.mybatis.domain.SwitchPort;

public class SwitchPortDTO extends SwitchPort {

    private String switchName;
    private String machineBrand;
    private String machineModel;
    private String machineSn;
    private String outband;

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }

    public String getMachineBrand() {
        return machineBrand;
    }

    public void setMachineBrand(String machineBrand) {
        this.machineBrand = machineBrand;
    }

    public String getMachineModel() {
        return machineModel;
    }

    public void setMachineModel(String machineModel) {
        this.machineModel = machineModel;
    }

    public String getMachineSn() {
        return machineSn;
    }

    public void setMachineSn(String machineSn) {
        this.machineSn = machineSn;
    }

    public String getOutband() {
        return outband;
    }

    public void setOutband(String outband) {
        this.outband = outband;
    }
}
