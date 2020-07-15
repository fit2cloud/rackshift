package io.rackshift.metal.sdk.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;
import java.util.Map;

public class HardwareEntity {
    private Integer fansNumber;
    private List<String> macAddress;
    private Integer cpuCores;
    private Double cpuGhz;
    private String machineName;
    private String machineType;
    private String machineModle;
    private String machineSerialNumber;
    private Integer powerNumber;
    private Integer powerWatt;
    private Map<String, Integer> memoryInfo;
    private List<String> immInfo;
    private List<Double> diskSize;

    public HardwareEntity() {
    }

    public Integer getFansNumber() {
        return this.fansNumber;
    }

    public void setFansNumber(Integer fansNumber) {
        this.fansNumber = fansNumber;
    }

    public List<String> getMacAddress() {
        return this.macAddress;
    }

    public void setMacAddress(List<String> macAddress) {
        this.macAddress = macAddress;
    }

    public Integer getCpuCores() {
        return this.cpuCores;
    }

    public void setCpuCores(Integer cpuCores) {
        this.cpuCores = cpuCores;
    }

    public Double getCpuGhz() {
        return this.cpuGhz;
    }

    public void setCpuGhz(Double cpuGhz) {
        this.cpuGhz = cpuGhz;
    }

    public String getMachineName() {
        return this.machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineType() {
        return this.machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public String getMachineModle() {
        return this.machineModle;
    }

    public void setMachineModle(String machineModle) {
        this.machineModle = machineModle;
    }

    public String getMachineSerialNumber() {
        return this.machineSerialNumber;
    }

    public void setMachineSerialNumber(String machineSerialNumber) {
        this.machineSerialNumber = machineSerialNumber;
    }

    public Integer getPowerNumber() {
        return this.powerNumber;
    }

    public void setPowerNumber(Integer powerNumber) {
        this.powerNumber = powerNumber;
    }

    public Map<String, Integer> getMemoryInfo() {
        return this.memoryInfo;
    }

    public void setMemoryInfo(Map<String, Integer> memoryInfo) {
        this.memoryInfo = memoryInfo;
    }

    public List<String> getImmInfo() {
        return this.immInfo;
    }

    public void setImmInfo(List<String> immInfo) {
        this.immInfo = immInfo;
    }

    public Integer getPowerWatt() {
        return this.powerWatt;
    }

    public void setPowerWatt(Integer powerWatt) {
        this.powerWatt = powerWatt;
    }

    public List<Double> getDiskSize() {
        return this.diskSize;
    }

    public void setDiskSize(List<Double> diskSize) {
        this.diskSize = diskSize;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}