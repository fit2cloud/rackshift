package io.rackshift.plugin.hp.model;

import com.google.gson.annotations.SerializedName;

public class HpCpuDTO {


    /**
     * proc_socket : Proc 1
     * proc_name :  Intel(R) Xeon(R) CPU E5-2690 v2 @ 3.00GHz
     * proc_status : OP_STATUS_OK
     * proc_speed : 3000
     * proc_num_cores_enabled : 10
     * proc_num_cores : 10
     * proc_num_threads : 20
     * proc_mem_technology : 64-bit Capable
     * proc_num_l1cache : 320
     * proc_num_l2cache : 2560
     * proc_num_l3cache : 25600
     */

    @SerializedName("proc_socket")
    private String procSocket;
    @SerializedName("proc_name")
    private String procName;
    @SerializedName("proc_status")
    private String procStatus;
    @SerializedName("proc_speed")
    private int procSpeed;
    @SerializedName("proc_num_cores_enabled")
    private int procNumCoresEnabled;
    @SerializedName("proc_num_cores")
    private int procNumCores;
    @SerializedName("proc_num_threads")
    private int procNumThreads;
    @SerializedName("proc_mem_technology")
    private String procMemTechnology;
    @SerializedName("proc_num_l1cache")
    private int procNumL1cache;
    @SerializedName("proc_num_l2cache")
    private int procNumL2cache;
    @SerializedName("proc_num_l3cache")
    private int procNumL3cache;

    public String getProcSocket() {
        return procSocket;
    }

    public void setProcSocket(String procSocket) {
        this.procSocket = procSocket;
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public String getProcStatus() {
        return procStatus;
    }

    public void setProcStatus(String procStatus) {
        this.procStatus = procStatus;
    }

    public int getProcSpeed() {
        return procSpeed;
    }

    public void setProcSpeed(int procSpeed) {
        this.procSpeed = procSpeed;
    }

    public int getProcNumCoresEnabled() {
        return procNumCoresEnabled;
    }

    public void setProcNumCoresEnabled(int procNumCoresEnabled) {
        this.procNumCoresEnabled = procNumCoresEnabled;
    }

    public int getProcNumCores() {
        return procNumCores;
    }

    public void setProcNumCores(int procNumCores) {
        this.procNumCores = procNumCores;
    }

    public int getProcNumThreads() {
        return procNumThreads;
    }

    public void setProcNumThreads(int procNumThreads) {
        this.procNumThreads = procNumThreads;
    }

    public String getProcMemTechnology() {
        return procMemTechnology;
    }

    public void setProcMemTechnology(String procMemTechnology) {
        this.procMemTechnology = procMemTechnology;
    }

    public int getProcNumL1cache() {
        return procNumL1cache;
    }

    public void setProcNumL1cache(int procNumL1cache) {
        this.procNumL1cache = procNumL1cache;
    }

    public int getProcNumL2cache() {
        return procNumL2cache;
    }

    public void setProcNumL2cache(int procNumL2cache) {
        this.procNumL2cache = procNumL2cache;
    }

    public int getProcNumL3cache() {
        return procNumL3cache;
    }

    public void setProcNumL3cache(int procNumL3cache) {
        this.procNumL3cache = procNumL3cache;
    }
}
