package io.rackshift.plugin.inspur.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InspurCpuDTO {


    /**
     * WEBVAR_STRUCTNAME_GETCPUINFO : [{"L3Cache":30720,"signature":"306f2","maxspeed":"4000MHz","L1Cache":768,"microcode":"00000036","used_core":12,"RatedPower":135,"CoreNumber":12,"L2Cache":3072,"CPUID":0,"Model":"Intel(R)Xeon(R)CPUE5-2690v3@2.60GHz","MainFrequency":0,"Present":1},{"L3Cache":30720,"signature":"306f2","maxspeed":"4000MHz","L1Cache":768,"microcode":"00000036","used_core":12,"RatedPower":135,"CoreNumber":12,"L2Cache":3072,"CPUID":1,"Model":"Intel(R)Xeon(R)CPUE5-2690v3@2.60GHz","MainFrequency":0,"Present":1},{}]
     * HAPI_STATUS : 0
     */

    @SerializedName("HAPI_STATUS")
    private int HAPISTATUS;
    @SerializedName("WEBVAR_STRUCTNAME_GETCPUINFO")
    private List<WEBVARSTRUCTNAMEGETCPUINFOBean> WEBVARSTRUCTNAMEGETCPUINFO;

    public int getHAPISTATUS() {
        return HAPISTATUS;
    }

    public void setHAPISTATUS(int HAPISTATUS) {
        this.HAPISTATUS = HAPISTATUS;
    }

    public List<WEBVARSTRUCTNAMEGETCPUINFOBean> getWEBVARSTRUCTNAMEGETCPUINFO() {
        return WEBVARSTRUCTNAMEGETCPUINFO;
    }

    public void setWEBVARSTRUCTNAMEGETCPUINFO(List<WEBVARSTRUCTNAMEGETCPUINFOBean> WEBVARSTRUCTNAMEGETCPUINFO) {
        this.WEBVARSTRUCTNAMEGETCPUINFO = WEBVARSTRUCTNAMEGETCPUINFO;
    }

    public static class WEBVARSTRUCTNAMEGETCPUINFOBean {
        /**
         * L3Cache : 30720
         * signature : 306f2
         * maxspeed : 4000MHz
         * L1Cache : 768
         * microcode : 00000036
         * used_core : 12
         * RatedPower : 135
         * CoreNumber : 12
         * L2Cache : 3072
         * CPUID : 0
         * Model : Intel(R)Xeon(R)CPUE5-2690v3@2.60GHz
         * MainFrequency : 0
         * Present : 1
         */

        @SerializedName("L3Cache")
        private int L3Cache;
        @SerializedName("signature")
        private String signature;
        @SerializedName("maxspeed")
        private String maxspeed;
        @SerializedName("L1Cache")
        private int L1Cache;
        @SerializedName("microcode")
        private String microcode;
        @SerializedName("used_core")
        private int usedCore;
        @SerializedName("RatedPower")
        private int RatedPower;
        @SerializedName("CoreNumber")
        private int CoreNumber;
        @SerializedName("L2Cache")
        private int L2Cache;
        @SerializedName("CPUID")
        private int CPUID;
        @SerializedName("Model")
        private String Model;
        @SerializedName("MainFrequency")
        private int MainFrequency;
        @SerializedName("Present")
        private int Present;

        public int getL3Cache() {
            return L3Cache;
        }

        public void setL3Cache(int L3Cache) {
            this.L3Cache = L3Cache;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getMaxspeed() {
            return maxspeed;
        }

        public void setMaxspeed(String maxspeed) {
            this.maxspeed = maxspeed;
        }

        public int getL1Cache() {
            return L1Cache;
        }

        public void setL1Cache(int L1Cache) {
            this.L1Cache = L1Cache;
        }

        public String getMicrocode() {
            return microcode;
        }

        public void setMicrocode(String microcode) {
            this.microcode = microcode;
        }

        public int getUsedCore() {
            return usedCore;
        }

        public void setUsedCore(int usedCore) {
            this.usedCore = usedCore;
        }

        public int getRatedPower() {
            return RatedPower;
        }

        public void setRatedPower(int RatedPower) {
            this.RatedPower = RatedPower;
        }

        public int getCoreNumber() {
            return CoreNumber;
        }

        public void setCoreNumber(int CoreNumber) {
            this.CoreNumber = CoreNumber;
        }

        public int getL2Cache() {
            return L2Cache;
        }

        public void setL2Cache(int L2Cache) {
            this.L2Cache = L2Cache;
        }

        public int getCPUID() {
            return CPUID;
        }

        public void setCPUID(int CPUID) {
            this.CPUID = CPUID;
        }

        public String getModel() {
            return Model;
        }

        public void setModel(String Model) {
            this.Model = Model;
        }

        public int getMainFrequency() {
            return MainFrequency;
        }

        public void setMainFrequency(int MainFrequency) {
            this.MainFrequency = MainFrequency;
        }

        public int getPresent() {
            return Present;
        }

        public void setPresent(int Present) {
            this.Present = Present;
        }
    }
}
