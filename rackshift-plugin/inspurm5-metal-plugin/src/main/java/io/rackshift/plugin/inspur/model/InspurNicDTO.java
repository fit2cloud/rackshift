package io.rackshift.plugin.inspur.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InspurNicDTO {


    /**
     * WEBVAR_STRUCTNAME_GETMACINFO : [{"Index":0,"nicType":0,"MACADDRESS":"6c:92:bf:25:2a:52","IPADDRESS":"0.0.0.0"},{"Index":1,"nicType":0,"MACADDRESS":"6c:92:bf:25:2a:53","IPADDRESS":"0.0.0.0"},{}]
     * HAPI_STATUS : 0
     */

    @SerializedName("HAPI_STATUS")
    private int HAPISTATUS;
    @SerializedName("WEBVAR_STRUCTNAME_GETMACINFO")
    private List<WEBVARSTRUCTNAMEGETMACINFOBean> WEBVARSTRUCTNAMEGETMACINFO;

    public int getHAPISTATUS() {
        return HAPISTATUS;
    }

    public void setHAPISTATUS(int HAPISTATUS) {
        this.HAPISTATUS = HAPISTATUS;
    }

    public List<WEBVARSTRUCTNAMEGETMACINFOBean> getWEBVARSTRUCTNAMEGETMACINFO() {
        return WEBVARSTRUCTNAMEGETMACINFO;
    }

    public void setWEBVARSTRUCTNAMEGETMACINFO(List<WEBVARSTRUCTNAMEGETMACINFOBean> WEBVARSTRUCTNAMEGETMACINFO) {
        this.WEBVARSTRUCTNAMEGETMACINFO = WEBVARSTRUCTNAMEGETMACINFO;
    }

    public static class WEBVARSTRUCTNAMEGETMACINFOBean {
        /**
         * Index : 0
         * nicType : 0
         * MACADDRESS : 6c:92:bf:25:2a:52
         * IPADDRESS : 0.0.0.0
         */

        @SerializedName("Index")
        private int Index;
        @SerializedName("nicType")
        private int nicType;
        @SerializedName("MACADDRESS")
        private String MACADDRESS;
        @SerializedName("IPADDRESS")
        private String IPADDRESS;

        public int getIndex() {
            return Index;
        }

        public void setIndex(int Index) {
            this.Index = Index;
        }

        public int getNicType() {
            return nicType;
        }

        public void setNicType(int nicType) {
            this.nicType = nicType;
        }

        public String getMACADDRESS() {
            return MACADDRESS;
        }

        public void setMACADDRESS(String MACADDRESS) {
            this.MACADDRESS = MACADDRESS;
        }

        public String getIPADDRESS() {
            return IPADDRESS;
        }

        public void setIPADDRESS(String IPADDRESS) {
            this.IPADDRESS = IPADDRESS;
        }
    }
}
