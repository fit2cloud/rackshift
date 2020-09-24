package io.rackshift.plugin.inspur.model.nf8480m4;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NF8480M4MemoryDTO {

    /**
     * WEBVAR_STRUCTNAME_GETMEMINFO : [{"MemDimm":"MEM0_C0D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM0_C0D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM0_C1D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM0_C1D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM0_C2D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM0_C2D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM0_C3D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM0_C3D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM1_C0D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM1_C0D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM1_C1D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM1_C1D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM1_C2D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM1_C2D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM1_C3D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM1_C3D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM2_C0D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM2_C0D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM2_C1D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM2_C1D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM2_C2D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM2_C2D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM2_C3D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM2_C3D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM3_C0D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM3_C0D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM3_C1D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM3_C1D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM3_C2D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM3_C2D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM3_C3D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM3_C3D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM4_C0D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM4_C0D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM4_C1D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM4_C1D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM4_C2D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM4_C2D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM4_C3D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM4_C3D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM5_C0D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM5_C0D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM5_C1D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM5_C1D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM5_C2D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM5_C2D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM5_C3D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM5_C3D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM6_C0D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM6_C0D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM6_C1D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM6_C1D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM6_C2D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM6_C2D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM6_C3D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM6_C3D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM7_C0D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM7_C0D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM7_C1D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM7_C1D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM7_C2D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM7_C2D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM7_C3D0","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{"MemDimm":"MEM7_C3D1","MemType":26,"MemManufacturer":"Samsung","memSize":16,"MemSpeed":2133,"MemCurrentSpeed":1333},{}]
     * HAPI_STATUS : 0
     */

    @SerializedName("HAPI_STATUS")
    private int HAPISTATUS;
    @SerializedName("WEBVAR_STRUCTNAME_GETMEMINFO")
    private List<WEBVARSTRUCTNAMEGETMEMINFOBean> WEBVARSTRUCTNAMEGETMEMINFO;

    public int getHAPISTATUS() {
        return HAPISTATUS;
    }

    public void setHAPISTATUS(int HAPISTATUS) {
        this.HAPISTATUS = HAPISTATUS;
    }

    public List<WEBVARSTRUCTNAMEGETMEMINFOBean> getWEBVARSTRUCTNAMEGETMEMINFO() {
        return WEBVARSTRUCTNAMEGETMEMINFO;
    }

    public void setWEBVARSTRUCTNAMEGETMEMINFO(List<WEBVARSTRUCTNAMEGETMEMINFOBean> WEBVARSTRUCTNAMEGETMEMINFO) {
        this.WEBVARSTRUCTNAMEGETMEMINFO = WEBVARSTRUCTNAMEGETMEMINFO;
    }

    public static class WEBVARSTRUCTNAMEGETMEMINFOBean {
        /**
         * MemDimm : MEM0_C0D0
         * MemType : 26
         * MemManufacturer : Samsung
         * memSize : 16
         * MemSpeed : 2133
         * MemCurrentSpeed : 1333
         */

        @SerializedName("MemDimm")
        private String MemDimm;
        @SerializedName("MemType")
        private int MemType;
        @SerializedName("MemManufacturer")
        private String MemManufacturer;
        @SerializedName("memSize")
        private int memSize;
        @SerializedName("MemSpeed")
        private int MemSpeed;
        @SerializedName("MemCurrentSpeed")
        private int MemCurrentSpeed;

        public String getMemDimm() {
            return MemDimm;
        }

        public void setMemDimm(String MemDimm) {
            this.MemDimm = MemDimm;
        }

        public int getMemType() {
            return MemType;
        }

        public void setMemType(int MemType) {
            this.MemType = MemType;
        }

        public String getMemManufacturer() {
            return MemManufacturer;
        }

        public void setMemManufacturer(String MemManufacturer) {
            this.MemManufacturer = MemManufacturer;
        }

        public int getMemSize() {
            return memSize;
        }

        public void setMemSize(int memSize) {
            this.memSize = memSize;
        }

        public int getMemSpeed() {
            return MemSpeed;
        }

        public void setMemSpeed(int MemSpeed) {
            this.MemSpeed = MemSpeed;
        }

        public int getMemCurrentSpeed() {
            return MemCurrentSpeed;
        }

        public void setMemCurrentSpeed(int MemCurrentSpeed) {
            this.MemCurrentSpeed = MemCurrentSpeed;
        }
    }
}
