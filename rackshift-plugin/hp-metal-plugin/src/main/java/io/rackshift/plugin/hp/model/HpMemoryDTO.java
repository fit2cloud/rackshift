package io.rackshift.plugin.hp.model;

import com.google.gson.annotations.SerializedName;

public class HpMemoryDTO {

    /**
     * mem_mod_idx : 0
     * mem_brd_num : 0
     * mem_cpu_num : 1
     * mem_riser_num : 0
     * mem_mod_num : 1
     * mem_mod_size : 16384
     * mem_mod_type : MEM_DIMM_DDR3
     * mem_mod_tech : MEM_RDIMM
     * mem_mod_frequency : 1600
     * mem_mod_status : MEM_GOOD_IN_USE
     * mem_mod_condition : MEM_OK
     * mem_mod_smartmem : MEM_YES
     * mem_mod_part_num : 713756-081
     * mem_mod_min_volt : 1350
     * mem_mod_ranks : 2
     */

    @SerializedName("mem_mod_idx")
    private int memModIdx;
    @SerializedName("mem_brd_num")
    private int memBrdNum;
    @SerializedName("mem_cpu_num")
    private int memCpuNum;
    @SerializedName("mem_riser_num")
    private int memRiserNum;
    @SerializedName("mem_mod_num")
    private int memModNum;
    @SerializedName("mem_mod_size")
    private int memModSize;
    @SerializedName("mem_mod_type")
    private String memModType;
    @SerializedName("mem_mod_tech")
    private String memModTech;
    @SerializedName("mem_mod_frequency")
    private int memModFrequency;
    @SerializedName("mem_mod_status")
    private String memModStatus;
    @SerializedName("mem_mod_condition")
    private String memModCondition;
    @SerializedName("mem_mod_smartmem")
    private String memModSmartmem;
    @SerializedName("mem_mod_part_num")
    private String memModPartNum;
    @SerializedName("mem_mod_min_volt")
    private int memModMinVolt;
    @SerializedName("mem_mod_ranks")
    private int memModRanks;

    public int getMemModIdx() {
        return memModIdx;
    }

    public void setMemModIdx(int memModIdx) {
        this.memModIdx = memModIdx;
    }

    public int getMemBrdNum() {
        return memBrdNum;
    }

    public void setMemBrdNum(int memBrdNum) {
        this.memBrdNum = memBrdNum;
    }

    public int getMemCpuNum() {
        return memCpuNum;
    }

    public void setMemCpuNum(int memCpuNum) {
        this.memCpuNum = memCpuNum;
    }

    public int getMemRiserNum() {
        return memRiserNum;
    }

    public void setMemRiserNum(int memRiserNum) {
        this.memRiserNum = memRiserNum;
    }

    public int getMemModNum() {
        return memModNum;
    }

    public void setMemModNum(int memModNum) {
        this.memModNum = memModNum;
    }

    public int getMemModSize() {
        return memModSize;
    }

    public void setMemModSize(int memModSize) {
        this.memModSize = memModSize / 1024;
    }

    public String getMemModType() {
        return memModType;
    }

    public void setMemModType(String memModType) {
        this.memModType = memModType.replace("MEM_DIMM_", "");
    }

    public String getMemModTech() {
        return memModTech;
    }

    public void setMemModTech(String memModTech) {
        this.memModTech = memModTech;
    }

    public int getMemModFrequency() {
        return memModFrequency;
    }

    public void setMemModFrequency(int memModFrequency) {
        this.memModFrequency = memModFrequency;
    }

    public String getMemModStatus() {
        return memModStatus;
    }

    public void setMemModStatus(String memModStatus) {
        this.memModStatus = memModStatus;
    }

    public String getMemModCondition() {
        return memModCondition;
    }

    public void setMemModCondition(String memModCondition) {
        this.memModCondition = memModCondition;
    }

    public String getMemModSmartmem() {
        return memModSmartmem;
    }

    public void setMemModSmartmem(String memModSmartmem) {
        this.memModSmartmem = memModSmartmem;
    }

    public String getMemModPartNum() {
        return memModPartNum;
    }

    public void setMemModPartNum(String memModPartNum) {
        this.memModPartNum = memModPartNum;
    }

    public int getMemModMinVolt() {
        return memModMinVolt;
    }

    public void setMemModMinVolt(int memModMinVolt) {
        this.memModMinVolt = memModMinVolt;
    }

    public int getMemModRanks() {
        return memModRanks;
    }

    public void setMemModRanks(int memModRanks) {
        this.memModRanks = memModRanks;
    }
}
