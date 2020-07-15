package io.rackshift.metal.sdk.model.request;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class BaseRequest {
    String host;
    String userName;
    String pwd;
    String newIp;
    String newPwd;
    String remark;
    String assetId;
    String machineRoom;
    String machineRack;
    String uNumber;
    String applyUser;

    public BaseRequest() {
    }

    public BaseRequest(String host, String userName, String pwd) {
        this.host = host;
        this.userName = userName;
        this.pwd = pwd;
    }

    public BaseRequest(String host, String userName, String pwd, String newIp, String newPwd, String remark, String assetId, String machineRoom, String machineRack, String uNumber, String applyUser) {
        this.host = host;
        this.userName = userName;
        this.pwd = pwd;
        this.newIp = newIp;
        this.newPwd = newPwd;
        this.remark = remark;
        this.assetId = assetId;
        this.machineRoom = machineRoom;
        this.machineRack = machineRack;
        this.uNumber = uNumber;
        this.applyUser = applyUser;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getNewIp() {
        return newIp;
    }

    public void setNewIp(String newIp) {
        this.newIp = newIp;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getMachineRoom() {
        return machineRoom;
    }

    public void setMachineRoom(String machineRoom) {
        this.machineRoom = machineRoom;
    }

    public String getMachineRack() {
        return machineRack;
    }

    public void setMachineRack(String machineRack) {
        this.machineRack = machineRack;
    }

    public String getuNumber() {
        return uNumber;
    }

    public void setuNumber(String uNumber) {
        this.uNumber = uNumber;
    }

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
