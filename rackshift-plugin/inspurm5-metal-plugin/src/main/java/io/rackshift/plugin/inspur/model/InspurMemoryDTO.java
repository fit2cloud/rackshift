package io.rackshift.plugin.inspur.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InspurMemoryDTO {


    /**
     * WEBVAR_STRUCTNAME_GETMEMINFO : [{"Speed":2133,"Capacity":16,"Present":1,"Manufacture":"Samsung","SN":"417B977F","MEMID":0},{"Speed":0,"Capacity":0,"Present":0,"Manufacture":"","SN":"","MEMID":1},{"Speed":0,"Capacity":0,"Present":0,"Manufacture":"","SN":"","MEMID":2},{"Speed":2133,"Capacity":16,"Present":1,"Manufacture":"Samsung","SN":"417B95B4","MEMID":3},{"Speed":0,"Capacity":0,"Present":0,"Manufacture":"","SN":"","MEMID":4},{"Speed":0,"Capacity":0,"Present":0,"Manufacture":"","SN":"","MEMID":5},{"Speed":2133,"Capacity":16,"Present":1,"Manufacture":"Samsung","SN":"417B955C","MEMID":6},{"Speed":0,"Capacity":0,"Present":0,"Manufacture":"","SN":"","MEMID":7},{"Speed":0,"Capacity":0,"Present":0,"Manufacture":"","SN":"","MEMID":8},{"Speed":2133,"Capacity":16,"Present":1,"Manufacture":"Samsung","SN":"417B9309","MEMID":9},{"Speed":0,"Capacity":0,"Present":0,"Manufacture":"","SN":"","MEMID":10},{"Speed":0,"Capacity":0,"Present":0,"Manufacture":"","SN":"","MEMID":11},{"Speed":2133,"Capacity":16,"Present":1,"Manufacture":"Samsung","SN":"417B9715","MEMID":12},{"Speed":0,"Capacity":0,"Present":0,"Manufacture":"","SN":"","MEMID":13},{"Speed":0,"Capacity":0,"Present":0,"Manufacture":"","SN":"","MEMID":14},{"Speed":2133,"Capacity":16,"Present":1,"Manufacture":"Samsung","SN":"417B96D4","MEMID":15},{"Speed":0,"Capacity":0,"Present":0,"Manufacture":"","SN":"","MEMID":16},{"Speed":0,"Capacity":0,"Present":0,"Manufacture":"","SN":"","MEMID":17},{"Speed":2133,"Capacity":16,"Present":1,"Manufacture":"Samsung","SN":"417B9788","MEMID":18},{"Speed":0,"Capacity":0,"Present":0,"Manufacture":"","SN":"","MEMID":19},{"Speed":0,"Capacity":0,"Present":0,"Manufacture":"","SN":"","MEMID":20},{"Speed":2133,"Capacity":16,"Present":1,"Manufacture":"Samsung","SN":"417B971A","MEMID":21},{"Speed":0,"Capacity":0,"Present":0,"Manufacture":"","SN":"","MEMID":22},{"Speed":0,"Capacity":0,"Present":0,"Manufacture":"","SN":"","MEMID":23},{}]
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
         * Speed : 2133
         * Capacity : 16
         * Present : 1
         * Manufacture : Samsung
         * SN : 417B977F
         * MEMID : 0
         */

        @SerializedName("Speed")
        private int Speed;
        @SerializedName("Capacity")
        private int Capacity;
        @SerializedName("Present")
        private int Present;
        @SerializedName("Manufacture")
        private String Manufacture;
        @SerializedName("SN")
        private String SN;
        @SerializedName("MEMID")
        private int MEMID;

        public int getSpeed() {
            return Speed;
        }

        public void setSpeed(int Speed) {
            this.Speed = Speed;
        }

        public int getCapacity() {
            return Capacity;
        }

        public void setCapacity(int Capacity) {
            this.Capacity = Capacity;
        }

        public int getPresent() {
            return Present;
        }

        public void setPresent(int Present) {
            this.Present = Present;
        }

        public String getManufacture() {
            return Manufacture;
        }

        public void setManufacture(String Manufacture) {
            this.Manufacture = Manufacture;
        }

        public String getSN() {
            return SN;
        }

        public void setSN(String SN) {
            this.SN = SN;
        }

        public int getMEMID() {
            return MEMID;
        }

        public void setMEMID(int MEMID) {
            this.MEMID = MEMID;
        }
    }
}
