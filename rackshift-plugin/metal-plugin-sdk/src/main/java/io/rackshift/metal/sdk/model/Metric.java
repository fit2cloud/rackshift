package io.rackshift.metal.sdk.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.LinkedList;
import java.util.List;

public class Metric {
    private List<Integer> cpuTemp = new LinkedList<>();
    private List<Integer> memoryTemp = new LinkedList<>();
    private List<Integer> memoryStatus = new LinkedList<>();
    private int mainBoardTemp;
    private List<Integer> powerTemp = new LinkedList<>();
    private List<Integer> powerStatus = new LinkedList<>();
    private List<Integer> fanStatus = new LinkedList<>();
    private List<Integer> disktatus = new LinkedList<>();
    private List<Integer> powerWatt = new LinkedList<>();
    private long selPercentUsed; // 日志使用百分比

    public long getSelPercentUsed() {
        return selPercentUsed;
    }

    public void setSelPercentUsed(long selPercentUsed) {
        this.selPercentUsed = selPercentUsed;
    }

    public List<Integer> getCpuTemp() {
        return cpuTemp;
    }

    public void setCpuTemp(List<Integer> cpuTemp) {
        this.cpuTemp = cpuTemp;
    }

    public List<Integer> getMemoryTemp() {
        return memoryTemp;
    }

    public void setMemoryTemp(List<Integer> memoryTemp) {
        this.memoryTemp = memoryTemp;
    }

    public int getMainBoardTemp() {
        return mainBoardTemp;
    }

    public void setMainBoardTemp(int mainBoardTemp) {
        this.mainBoardTemp = mainBoardTemp;
    }

    public List<Integer> getPowerTemp() {
        return powerTemp;
    }

    public void setPowerTemp(List<Integer> powerTemp) {
        this.powerTemp = powerTemp;
    }

    public List<Integer> getPowerWatt() {
        return powerWatt;
    }

    public void setPowerWatt(List<Integer> powerWatt) {
        this.powerWatt = powerWatt;
    }

    public List<Integer> getPowerStatus() {
        return powerStatus;
    }

    public void setPowerStatus(List<Integer> powerStatus) {
        this.powerStatus = powerStatus;
    }

    public List<Integer> getFanStatus() {
        return fanStatus;
    }

    public void setFanStatus(List<Integer> fanStatus) {
        this.fanStatus = fanStatus;
    }

    public List<Integer> getDisktatus() {
        return disktatus;
    }

    public void setDisktatus(List<Integer> disktatus) {
        this.disktatus = disktatus;
    }

    public List<Integer> getMemoryStatus() {
        return memoryStatus;
    }

    public void setMemoryStatus(List<Integer> memoryStatus) {
        this.memoryStatus = memoryStatus;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
