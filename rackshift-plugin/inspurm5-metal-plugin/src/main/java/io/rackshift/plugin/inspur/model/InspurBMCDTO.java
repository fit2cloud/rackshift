package io.rackshift.plugin.inspur.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InspurBMCDTO {


    /**
     * WEBVAR_STRUCTNAME_GETALLNETWORKCFG : [{"channelNum":8,"lanEnable":0,"macAddress":"00:00:00:00:00:00","v4IPSource":0,"v4IPAddr":"0.0.0.0","v4Subnet":"0.0.0.0","v4Gateway":"0.0.0.0","v6Enable":0,"v6IPSource":0,"v6IPAddr":"::","v6Prefix":0,"v6Gateway":"::","vlanEnable":0,"vlanID":0,"vlanPriority":0},{"channelNum":1,"lanEnable":1,"macAddress":"6C:92:BF:25:2A:55","v4IPSource":1,"v4IPAddr":"10.132.47.212","v4Subnet":"255.255.255.0","v4Gateway":"10.132.47.3","v6Enable":1,"v6IPSource":2,"v6IPAddr":"::","v6Prefix":0,"v6Gateway":"::","vlanEnable":0,"vlanID":0,"vlanPriority":0},{}]
     * HAPI_STATUS : 0
     */

    @SerializedName("HAPI_STATUS")
    private int HAPISTATUS;
    @SerializedName("WEBVAR_STRUCTNAME_GETALLNETWORKCFG")
    private List<WEBVARSTRUCTNAMEGETALLNETWORKCFGBean> WEBVARSTRUCTNAMEGETALLNETWORKCFG;

    public int getHAPISTATUS() {
        return HAPISTATUS;
    }

    public void setHAPISTATUS(int HAPISTATUS) {
        this.HAPISTATUS = HAPISTATUS;
    }

    public List<WEBVARSTRUCTNAMEGETALLNETWORKCFGBean> getWEBVARSTRUCTNAMEGETALLNETWORKCFG() {
        return WEBVARSTRUCTNAMEGETALLNETWORKCFG;
    }

    public void setWEBVARSTRUCTNAMEGETALLNETWORKCFG(List<WEBVARSTRUCTNAMEGETALLNETWORKCFGBean> WEBVARSTRUCTNAMEGETALLNETWORKCFG) {
        this.WEBVARSTRUCTNAMEGETALLNETWORKCFG = WEBVARSTRUCTNAMEGETALLNETWORKCFG;
    }

    public static class WEBVARSTRUCTNAMEGETALLNETWORKCFGBean {
        /**
         * channelNum : 8
         * lanEnable : 0
         * macAddress : 00:00:00:00:00:00
         * v4IPSource : 0
         * v4IPAddr : 0.0.0.0
         * v4Subnet : 0.0.0.0
         * v4Gateway : 0.0.0.0
         * v6Enable : 0
         * v6IPSource : 0
         * v6IPAddr : ::
         * v6Prefix : 0
         * v6Gateway : ::
         * vlanEnable : 0
         * vlanID : 0
         * vlanPriority : 0
         */

        @SerializedName("channelNum")
        private int channelNum;
        @SerializedName("lanEnable")
        private int lanEnable;
        @SerializedName("macAddress")
        private String macAddress;
        @SerializedName("v4IPSource")
        private int v4IPSource;
        @SerializedName("v4IPAddr")
        private String v4IPAddr;
        @SerializedName("v4Subnet")
        private String v4Subnet;
        @SerializedName("v4Gateway")
        private String v4Gateway;
        @SerializedName("v6Enable")
        private int v6Enable;
        @SerializedName("v6IPSource")
        private int v6IPSource;
        @SerializedName("v6IPAddr")
        private String v6IPAddr;
        @SerializedName("v6Prefix")
        private int v6Prefix;
        @SerializedName("v6Gateway")
        private String v6Gateway;
        @SerializedName("vlanEnable")
        private int vlanEnable;
        @SerializedName("vlanID")
        private int vlanID;
        @SerializedName("vlanPriority")
        private int vlanPriority;

        public int getChannelNum() {
            return channelNum;
        }

        public void setChannelNum(int channelNum) {
            this.channelNum = channelNum;
        }

        public int getLanEnable() {
            return lanEnable;
        }

        public void setLanEnable(int lanEnable) {
            this.lanEnable = lanEnable;
        }

        public String getMacAddress() {
            return macAddress;
        }

        public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
        }

        public int getV4IPSource() {
            return v4IPSource;
        }

        public void setV4IPSource(int v4IPSource) {
            this.v4IPSource = v4IPSource;
        }

        public String getV4IPAddr() {
            return v4IPAddr;
        }

        public void setV4IPAddr(String v4IPAddr) {
            this.v4IPAddr = v4IPAddr;
        }

        public String getV4Subnet() {
            return v4Subnet;
        }

        public void setV4Subnet(String v4Subnet) {
            this.v4Subnet = v4Subnet;
        }

        public String getV4Gateway() {
            return v4Gateway;
        }

        public void setV4Gateway(String v4Gateway) {
            this.v4Gateway = v4Gateway;
        }

        public int getV6Enable() {
            return v6Enable;
        }

        public void setV6Enable(int v6Enable) {
            this.v6Enable = v6Enable;
        }

        public int getV6IPSource() {
            return v6IPSource;
        }

        public void setV6IPSource(int v6IPSource) {
            this.v6IPSource = v6IPSource;
        }

        public String getV6IPAddr() {
            return v6IPAddr;
        }

        public void setV6IPAddr(String v6IPAddr) {
            this.v6IPAddr = v6IPAddr;
        }

        public int getV6Prefix() {
            return v6Prefix;
        }

        public void setV6Prefix(int v6Prefix) {
            this.v6Prefix = v6Prefix;
        }

        public String getV6Gateway() {
            return v6Gateway;
        }

        public void setV6Gateway(String v6Gateway) {
            this.v6Gateway = v6Gateway;
        }

        public int getVlanEnable() {
            return vlanEnable;
        }

        public void setVlanEnable(int vlanEnable) {
            this.vlanEnable = vlanEnable;
        }

        public int getVlanID() {
            return vlanID;
        }

        public void setVlanID(int vlanID) {
            this.vlanID = vlanID;
        }

        public int getVlanPriority() {
            return vlanPriority;
        }

        public void setVlanPriority(int vlanPriority) {
            this.vlanPriority = vlanPriority;
        }
    }
}
