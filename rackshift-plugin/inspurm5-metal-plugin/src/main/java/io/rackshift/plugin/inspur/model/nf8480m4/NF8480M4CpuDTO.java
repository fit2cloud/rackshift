package io.rackshift.plugin.inspur.model.nf8480m4;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NF8480M4CpuDTO {

    /**
     * WEBVAR_STRUCTNAME_CPUINFO : [{"CPUSocket":"CPU0","CPUType":3,"L1Cache":64,"L2Cache":256,"L3Cache":35840,"CPUVersion":"Intel(R)Xeon(R)CPUE7-4850v3@2.20GHz"},{"CPUSocket":"CPU1","CPUType":3,"L1Cache":64,"L2Cache":256,"L3Cache":35840,"CPUVersion":"Intel(R)Xeon(R)CPUE7-4850v3@2.20GHz"},{"CPUSocket":"CPU2","CPUType":3,"L1Cache":64,"L2Cache":256,"L3Cache":35840,"CPUVersion":"Intel(R)Xeon(R)CPUE7-4850v3@2.20GHz"},{"CPUSocket":"CPU3","CPUType":3,"L1Cache":64,"L2Cache":256,"L3Cache":35840,"CPUVersion":"Intel(R)Xeon(R)CPUE7-4850v3@2.20GHz"},{}]
     * HAPI_STATUS : 0
     */

    @SerializedName("HAPI_STATUS")
    private int HAPISTATUS;
    @SerializedName("WEBVAR_STRUCTNAME_CPUINFO")
    private List<WEBVARSTRUCTNAMECPUINFOBean> WEBVARSTRUCTNAMECPUINFO;

    public int getHAPISTATUS() {
        return HAPISTATUS;
    }

    public void setHAPISTATUS(int HAPISTATUS) {
        this.HAPISTATUS = HAPISTATUS;
    }

    public List<WEBVARSTRUCTNAMECPUINFOBean> getWEBVARSTRUCTNAMECPUINFO() {
        return WEBVARSTRUCTNAMECPUINFO;
    }

    public void setWEBVARSTRUCTNAMECPUINFO(List<WEBVARSTRUCTNAMECPUINFOBean> WEBVARSTRUCTNAMECPUINFO) {
        this.WEBVARSTRUCTNAMECPUINFO = WEBVARSTRUCTNAMECPUINFO;
    }

    public static class WEBVARSTRUCTNAMECPUINFOBean {
        /**
         * CPUSocket : CPU0
         * CPUType : 3
         * L1Cache : 64
         * L2Cache : 256
         * L3Cache : 35840
         * CPUVersion : Intel(R)Xeon(R)CPUE7-4850v3@2.20GHz
         */

        @SerializedName("CPUSocket")
        private String CPUSocket;
        @SerializedName("CPUType")
        private int CPUType;
        @SerializedName("L1Cache")
        private int L1Cache;
        @SerializedName("L2Cache")
        private int L2Cache;
        @SerializedName("L3Cache")
        private int L3Cache;
        @SerializedName("CPUVersion")
        private String CPUVersion;

        public String getCPUSocket() {
            return CPUSocket;
        }

        public void setCPUSocket(String CPUSocket) {
            this.CPUSocket = CPUSocket;
        }

        public int getCPUType() {
            return CPUType;
        }

        public void setCPUType(int CPUType) {
            this.CPUType = CPUType;
        }

        public int getL1Cache() {
            return L1Cache;
        }

        public void setL1Cache(int L1Cache) {
            this.L1Cache = L1Cache;
        }

        public int getL2Cache() {
            return L2Cache;
        }

        public void setL2Cache(int L2Cache) {
            this.L2Cache = L2Cache;
        }

        public int getL3Cache() {
            return L3Cache;
        }

        public void setL3Cache(int L3Cache) {
            this.L3Cache = L3Cache;
        }

        public String getCPUVersion() {
            return CPUVersion;
        }

        public void setCPUVersion(String CPUVersion) {
            this.CPUVersion = CPUVersion;
        }
    }
}
