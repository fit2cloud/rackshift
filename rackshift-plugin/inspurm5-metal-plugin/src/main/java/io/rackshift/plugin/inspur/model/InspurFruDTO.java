package io.rackshift.plugin.inspur.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InspurFruDTO {

    /**
     * WEBVAR_STRUCTNAME_HL_GETALLFRUINFO : [{"FRUDeviceID":0,"FRUDeviceName":"BMC_FRU","CH_CommonHeaderFormatVersion":1,"CH_InternalUseAreaStartOffset":0,"CH_ChassisInfoAreaStartOffset":1,"CH_BoardInfoAreaStartOffset":3,"CH_ProductInfoAreaStartOffset":9,"CH_MultiRecordAreaStartOffset":0,"CI_ChassisInfoAreaFormatVersion":1,"CI_ChassisInfoAreaLength":2,"CI_ChassisType":"Rack Mount Chassis","CI_ChassisPartNum":"0","CI_ChassisSerialNum":"0","CI_CustomFields":"NULL\n","BI_BoardInfoAreaFormatVersion":1,"BI_BoardInfoAreaLength":6,"BI_Language":0,"BI_MfgDateTime":"Mon Sep  8 22:17:00 2014\n","BI_BoardMfr":"Inspur","BI_BoardProductName":"YZMB-00326-101","BI_BoardSerialNum":"0","BI_BoardPartNum":"NF5280M4","BI_FRUFileID":"","BI_CustomFields":"(null)","PI_ProductInfoAreaFormatVersion":1,"PI_ProductInfoAreaLength":6,"PI_Language":0,"PI_MfrName":"Inspur","PI_ProductName":"NF5280M4","PI_ProductPartNum":"0","PI_ProductVersion":"01","PI_ProductSerialNum":"815166989","PI_AssetTag":"815166989","PI_FRUFileID":"","PI_CustomFields":"(null)"},{}]
     * HAPI_STATUS : 0
     */

    @SerializedName("HAPI_STATUS")
    private int HAPISTATUS;
    @SerializedName("WEBVAR_STRUCTNAME_HL_GETALLFRUINFO")
    private List<WEBVARSTRUCTNAMEHLGETALLFRUINFOBean> WEBVARSTRUCTNAMEHLGETALLFRUINFO;

    public int getHAPISTATUS() {
        return HAPISTATUS;
    }

    public void setHAPISTATUS(int HAPISTATUS) {
        this.HAPISTATUS = HAPISTATUS;
    }

    public List<WEBVARSTRUCTNAMEHLGETALLFRUINFOBean> getWEBVARSTRUCTNAMEHLGETALLFRUINFO() {
        return WEBVARSTRUCTNAMEHLGETALLFRUINFO;
    }

    public void setWEBVARSTRUCTNAMEHLGETALLFRUINFO(List<WEBVARSTRUCTNAMEHLGETALLFRUINFOBean> WEBVARSTRUCTNAMEHLGETALLFRUINFO) {
        this.WEBVARSTRUCTNAMEHLGETALLFRUINFO = WEBVARSTRUCTNAMEHLGETALLFRUINFO;
    }

    public static class WEBVARSTRUCTNAMEHLGETALLFRUINFOBean {
        /**
         * FRUDeviceID : 0
         * FRUDeviceName : BMC_FRU
         * CH_CommonHeaderFormatVersion : 1
         * CH_InternalUseAreaStartOffset : 0
         * CH_ChassisInfoAreaStartOffset : 1
         * CH_BoardInfoAreaStartOffset : 3
         * CH_ProductInfoAreaStartOffset : 9
         * CH_MultiRecordAreaStartOffset : 0
         * CI_ChassisInfoAreaFormatVersion : 1
         * CI_ChassisInfoAreaLength : 2
         * CI_ChassisType : Rack Mount Chassis
         * CI_ChassisPartNum : 0
         * CI_ChassisSerialNum : 0
         * CI_CustomFields : NULL
         * BI_BoardInfoAreaFormatVersion : 1
         * BI_BoardInfoAreaLength : 6
         * BI_Language : 0
         * BI_MfgDateTime : Mon Sep  8 22:17:00 2014
         * BI_BoardMfr : Inspur
         * BI_BoardProductName : YZMB-00326-101
         * BI_BoardSerialNum : 0
         * BI_BoardPartNum : NF5280M4
         * BI_FRUFileID :
         * BI_CustomFields : (null)
         * PI_ProductInfoAreaFormatVersion : 1
         * PI_ProductInfoAreaLength : 6
         * PI_Language : 0
         * PI_MfrName : Inspur
         * PI_ProductName : NF5280M4
         * PI_ProductPartNum : 0
         * PI_ProductVersion : 01
         * PI_ProductSerialNum : 815166989
         * PI_AssetTag : 815166989
         * PI_FRUFileID :
         * PI_CustomFields : (null)
         */

        @SerializedName("FRUDeviceID")
        private int FRUDeviceID;
        @SerializedName("FRUDeviceName")
        private String FRUDeviceName;
        @SerializedName("CH_CommonHeaderFormatVersion")
        private int CHCommonHeaderFormatVersion;
        @SerializedName("CH_InternalUseAreaStartOffset")
        private int CHInternalUseAreaStartOffset;
        @SerializedName("CH_ChassisInfoAreaStartOffset")
        private int CHChassisInfoAreaStartOffset;
        @SerializedName("CH_BoardInfoAreaStartOffset")
        private int CHBoardInfoAreaStartOffset;
        @SerializedName("CH_ProductInfoAreaStartOffset")
        private int CHProductInfoAreaStartOffset;
        @SerializedName("CH_MultiRecordAreaStartOffset")
        private int CHMultiRecordAreaStartOffset;
        @SerializedName("CI_ChassisInfoAreaFormatVersion")
        private int CIChassisInfoAreaFormatVersion;
        @SerializedName("CI_ChassisInfoAreaLength")
        private int CIChassisInfoAreaLength;
        @SerializedName("CI_ChassisType")
        private String CIChassisType;
        @SerializedName("CI_ChassisPartNum")
        private String CIChassisPartNum;
        @SerializedName("CI_ChassisSerialNum")
        private String CIChassisSerialNum;
        @SerializedName("CI_CustomFields")
        private String CICustomFields;
        @SerializedName("BI_BoardInfoAreaFormatVersion")
        private int BIBoardInfoAreaFormatVersion;
        @SerializedName("BI_BoardInfoAreaLength")
        private int BIBoardInfoAreaLength;
        @SerializedName("BI_Language")
        private int BILanguage;
        @SerializedName("BI_MfgDateTime")
        private String BIMfgDateTime;
        @SerializedName("BI_BoardMfr")
        private String BIBoardMfr;
        @SerializedName("BI_BoardProductName")
        private String BIBoardProductName;
        @SerializedName("BI_BoardSerialNum")
        private String BIBoardSerialNum;
        @SerializedName("BI_BoardPartNum")
        private String BIBoardPartNum;
        @SerializedName("BI_FRUFileID")
        private String BIFRUFileID;
        @SerializedName("BI_CustomFields")
        private String BICustomFields;
        @SerializedName("PI_ProductInfoAreaFormatVersion")
        private int PIProductInfoAreaFormatVersion;
        @SerializedName("PI_ProductInfoAreaLength")
        private int PIProductInfoAreaLength;
        @SerializedName("PI_Language")
        private int PILanguage;
        @SerializedName("PI_MfrName")
        private String PIMfrName;
        @SerializedName("PI_ProductName")
        private String PIProductName;
        @SerializedName("PI_ProductPartNum")
        private String PIProductPartNum;
        @SerializedName("PI_ProductVersion")
        private String PIProductVersion;
        @SerializedName("PI_ProductSerialNum")
        private String PIProductSerialNum;
        @SerializedName("PI_AssetTag")
        private String PIAssetTag;
        @SerializedName("PI_FRUFileID")
        private String PIFRUFileID;
        @SerializedName("PI_CustomFields")
        private String PICustomFields;

        public int getFRUDeviceID() {
            return FRUDeviceID;
        }

        public void setFRUDeviceID(int FRUDeviceID) {
            this.FRUDeviceID = FRUDeviceID;
        }

        public String getFRUDeviceName() {
            return FRUDeviceName;
        }

        public void setFRUDeviceName(String FRUDeviceName) {
            this.FRUDeviceName = FRUDeviceName;
        }

        public int getCHCommonHeaderFormatVersion() {
            return CHCommonHeaderFormatVersion;
        }

        public void setCHCommonHeaderFormatVersion(int CHCommonHeaderFormatVersion) {
            this.CHCommonHeaderFormatVersion = CHCommonHeaderFormatVersion;
        }

        public int getCHInternalUseAreaStartOffset() {
            return CHInternalUseAreaStartOffset;
        }

        public void setCHInternalUseAreaStartOffset(int CHInternalUseAreaStartOffset) {
            this.CHInternalUseAreaStartOffset = CHInternalUseAreaStartOffset;
        }

        public int getCHChassisInfoAreaStartOffset() {
            return CHChassisInfoAreaStartOffset;
        }

        public void setCHChassisInfoAreaStartOffset(int CHChassisInfoAreaStartOffset) {
            this.CHChassisInfoAreaStartOffset = CHChassisInfoAreaStartOffset;
        }

        public int getCHBoardInfoAreaStartOffset() {
            return CHBoardInfoAreaStartOffset;
        }

        public void setCHBoardInfoAreaStartOffset(int CHBoardInfoAreaStartOffset) {
            this.CHBoardInfoAreaStartOffset = CHBoardInfoAreaStartOffset;
        }

        public int getCHProductInfoAreaStartOffset() {
            return CHProductInfoAreaStartOffset;
        }

        public void setCHProductInfoAreaStartOffset(int CHProductInfoAreaStartOffset) {
            this.CHProductInfoAreaStartOffset = CHProductInfoAreaStartOffset;
        }

        public int getCHMultiRecordAreaStartOffset() {
            return CHMultiRecordAreaStartOffset;
        }

        public void setCHMultiRecordAreaStartOffset(int CHMultiRecordAreaStartOffset) {
            this.CHMultiRecordAreaStartOffset = CHMultiRecordAreaStartOffset;
        }

        public int getCIChassisInfoAreaFormatVersion() {
            return CIChassisInfoAreaFormatVersion;
        }

        public void setCIChassisInfoAreaFormatVersion(int CIChassisInfoAreaFormatVersion) {
            this.CIChassisInfoAreaFormatVersion = CIChassisInfoAreaFormatVersion;
        }

        public int getCIChassisInfoAreaLength() {
            return CIChassisInfoAreaLength;
        }

        public void setCIChassisInfoAreaLength(int CIChassisInfoAreaLength) {
            this.CIChassisInfoAreaLength = CIChassisInfoAreaLength;
        }

        public String getCIChassisType() {
            return CIChassisType;
        }

        public void setCIChassisType(String CIChassisType) {
            this.CIChassisType = CIChassisType;
        }

        public String getCIChassisPartNum() {
            return CIChassisPartNum;
        }

        public void setCIChassisPartNum(String CIChassisPartNum) {
            this.CIChassisPartNum = CIChassisPartNum;
        }

        public String getCIChassisSerialNum() {
            return CIChassisSerialNum;
        }

        public void setCIChassisSerialNum(String CIChassisSerialNum) {
            this.CIChassisSerialNum = CIChassisSerialNum;
        }

        public String getCICustomFields() {
            return CICustomFields;
        }

        public void setCICustomFields(String CICustomFields) {
            this.CICustomFields = CICustomFields;
        }

        public int getBIBoardInfoAreaFormatVersion() {
            return BIBoardInfoAreaFormatVersion;
        }

        public void setBIBoardInfoAreaFormatVersion(int BIBoardInfoAreaFormatVersion) {
            this.BIBoardInfoAreaFormatVersion = BIBoardInfoAreaFormatVersion;
        }

        public int getBIBoardInfoAreaLength() {
            return BIBoardInfoAreaLength;
        }

        public void setBIBoardInfoAreaLength(int BIBoardInfoAreaLength) {
            this.BIBoardInfoAreaLength = BIBoardInfoAreaLength;
        }

        public int getBILanguage() {
            return BILanguage;
        }

        public void setBILanguage(int BILanguage) {
            this.BILanguage = BILanguage;
        }

        public String getBIMfgDateTime() {
            return BIMfgDateTime;
        }

        public void setBIMfgDateTime(String BIMfgDateTime) {
            this.BIMfgDateTime = BIMfgDateTime;
        }

        public String getBIBoardMfr() {
            return BIBoardMfr;
        }

        public void setBIBoardMfr(String BIBoardMfr) {
            this.BIBoardMfr = BIBoardMfr;
        }

        public String getBIBoardProductName() {
            return BIBoardProductName;
        }

        public void setBIBoardProductName(String BIBoardProductName) {
            this.BIBoardProductName = BIBoardProductName;
        }

        public String getBIBoardSerialNum() {
            return BIBoardSerialNum;
        }

        public void setBIBoardSerialNum(String BIBoardSerialNum) {
            this.BIBoardSerialNum = BIBoardSerialNum;
        }

        public String getBIBoardPartNum() {
            return BIBoardPartNum;
        }

        public void setBIBoardPartNum(String BIBoardPartNum) {
            this.BIBoardPartNum = BIBoardPartNum;
        }

        public String getBIFRUFileID() {
            return BIFRUFileID;
        }

        public void setBIFRUFileID(String BIFRUFileID) {
            this.BIFRUFileID = BIFRUFileID;
        }

        public String getBICustomFields() {
            return BICustomFields;
        }

        public void setBICustomFields(String BICustomFields) {
            this.BICustomFields = BICustomFields;
        }

        public int getPIProductInfoAreaFormatVersion() {
            return PIProductInfoAreaFormatVersion;
        }

        public void setPIProductInfoAreaFormatVersion(int PIProductInfoAreaFormatVersion) {
            this.PIProductInfoAreaFormatVersion = PIProductInfoAreaFormatVersion;
        }

        public int getPIProductInfoAreaLength() {
            return PIProductInfoAreaLength;
        }

        public void setPIProductInfoAreaLength(int PIProductInfoAreaLength) {
            this.PIProductInfoAreaLength = PIProductInfoAreaLength;
        }

        public int getPILanguage() {
            return PILanguage;
        }

        public void setPILanguage(int PILanguage) {
            this.PILanguage = PILanguage;
        }

        public String getPIMfrName() {
            return PIMfrName;
        }

        public void setPIMfrName(String PIMfrName) {
            this.PIMfrName = PIMfrName;
        }

        public String getPIProductName() {
            return PIProductName;
        }

        public void setPIProductName(String PIProductName) {
            this.PIProductName = PIProductName;
        }

        public String getPIProductPartNum() {
            return PIProductPartNum;
        }

        public void setPIProductPartNum(String PIProductPartNum) {
            this.PIProductPartNum = PIProductPartNum;
        }

        public String getPIProductVersion() {
            return PIProductVersion;
        }

        public void setPIProductVersion(String PIProductVersion) {
            this.PIProductVersion = PIProductVersion;
        }

        public String getPIProductSerialNum() {
            return PIProductSerialNum;
        }

        public void setPIProductSerialNum(String PIProductSerialNum) {
            this.PIProductSerialNum = PIProductSerialNum;
        }

        public String getPIAssetTag() {
            return PIAssetTag;
        }

        public void setPIAssetTag(String PIAssetTag) {
            this.PIAssetTag = PIAssetTag;
        }

        public String getPIFRUFileID() {
            return PIFRUFileID;
        }

        public void setPIFRUFileID(String PIFRUFileID) {
            this.PIFRUFileID = PIFRUFileID;
        }

        public String getPICustomFields() {
            return PICustomFields;
        }

        public void setPICustomFields(String PICustomFields) {
            this.PICustomFields = PICustomFields;
        }
    }
}
