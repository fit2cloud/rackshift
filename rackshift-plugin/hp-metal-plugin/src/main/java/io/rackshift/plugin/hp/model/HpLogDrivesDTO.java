package io.rackshift.plugin.hp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HpLogDrivesDTO {

    /**
     * hostpwr_state : ON
     * in_post : 0
     * ams_ready : AMS_UNAVAILABLE
     * data_state : DATA_NOT_AVAILABLE
     * log_drive_arrays : [{"storage_type":"SMART_ARRAY_CONTROLLER_TYPE","name":"Controller on System Board","status":"OP_STATUS_OK","hw_status":"OP_STATUS_OK","serial_no":"001438034621F00","model":"HP Smart Array P420i Controller","fw_version":"7.02","accel_cond":"OP_STATUS_OK","accel_serial":"PBKUC0BRH7X4DR","accel_tot_mem":"1048576 KB","has_accel":1,"has_encrypt":0,"logical_drives":[{"name":"Logical Drive 01","status":"OP_STATUS_OK","capacity":"1676 GB","log_status":"LOG_OK","flt_tol":"RAID 5","lu_cache_flag":"LOG_CACHE_FLAG_NONMEMBER","lu_cache_LUN":"HIDDEN","encr_stat":"ENCR_NOT_ENCR","physical_drives":[0,1,2,3]}],"enclosures":[{"name":"Drive Enclosure Port 1I Box 2","status":"OP_STATUS_OK","ports":"4"},{"name":"Drive Enclosure Port 2I Box 2","status":"OP_STATUS_OK","ports":"4"}]}]
     */

    @SerializedName("hostpwr_state")
    private String hostpwrState;
    @SerializedName("in_post")
    private int inPost;
    @SerializedName("ams_ready")
    private String amsReady;
    @SerializedName("data_state")
    private String dataState;
    @SerializedName("log_drive_arrays")
    private List<LogDriveArraysBean> logDriveArrays;

    public String getHostpwrState() {
        return hostpwrState;
    }

    public void setHostpwrState(String hostpwrState) {
        this.hostpwrState = hostpwrState;
    }

    public int getInPost() {
        return inPost;
    }

    public void setInPost(int inPost) {
        this.inPost = inPost;
    }

    public String getAmsReady() {
        return amsReady;
    }

    public void setAmsReady(String amsReady) {
        this.amsReady = amsReady;
    }

    public String getDataState() {
        return dataState;
    }

    public void setDataState(String dataState) {
        this.dataState = dataState;
    }

    public List<LogDriveArraysBean> getLogDriveArrays() {
        return logDriveArrays;
    }

    public void setLogDriveArrays(List<LogDriveArraysBean> logDriveArrays) {
        this.logDriveArrays = logDriveArrays;
    }

    public static class LogDriveArraysBean {
        /**
         * storage_type : SMART_ARRAY_CONTROLLER_TYPE
         * name : Controller on System Board
         * status : OP_STATUS_OK
         * hw_status : OP_STATUS_OK
         * serial_no : 001438034621F00
         * model : HP Smart Array P420i Controller
         * fw_version : 7.02
         * accel_cond : OP_STATUS_OK
         * accel_serial : PBKUC0BRH7X4DR
         * accel_tot_mem : 1048576 KB
         * has_accel : 1
         * has_encrypt : 0
         * logical_drives : [{"name":"Logical Drive 01","status":"OP_STATUS_OK","capacity":"1676 GB","log_status":"LOG_OK","flt_tol":"RAID 5","lu_cache_flag":"LOG_CACHE_FLAG_NONMEMBER","lu_cache_LUN":"HIDDEN","encr_stat":"ENCR_NOT_ENCR","physical_drives":[0,1,2,3]}]
         * enclosures : [{"name":"Drive Enclosure Port 1I Box 2","status":"OP_STATUS_OK","ports":"4"},{"name":"Drive Enclosure Port 2I Box 2","status":"OP_STATUS_OK","ports":"4"}]
         */

        @SerializedName("storage_type")
        private String storageType;
        @SerializedName("name")
        private String name;
        @SerializedName("status")
        private String status;
        @SerializedName("hw_status")
        private String hwStatus;
        @SerializedName("serial_no")
        private String serialNo;
        @SerializedName("model")
        private String model;
        @SerializedName("fw_version")
        private String fwVersion;
        @SerializedName("accel_cond")
        private String accelCond;
        @SerializedName("accel_serial")
        private String accelSerial;
        @SerializedName("accel_tot_mem")
        private String accelTotMem;
        @SerializedName("has_accel")
        private int hasAccel;
        @SerializedName("has_encrypt")
        private int hasEncrypt;
        @SerializedName("logical_drives")
        private List<LogicalDrivesBean> logicalDrives;
        @SerializedName("enclosures")
        private List<EnclosuresBean> enclosures;

        public String getStorageType() {
            return storageType;
        }

        public void setStorageType(String storageType) {
            this.storageType = storageType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getHwStatus() {
            return hwStatus;
        }

        public void setHwStatus(String hwStatus) {
            this.hwStatus = hwStatus;
        }

        public String getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(String serialNo) {
            this.serialNo = serialNo;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getFwVersion() {
            return fwVersion;
        }

        public void setFwVersion(String fwVersion) {
            this.fwVersion = fwVersion;
        }

        public String getAccelCond() {
            return accelCond;
        }

        public void setAccelCond(String accelCond) {
            this.accelCond = accelCond;
        }

        public String getAccelSerial() {
            return accelSerial;
        }

        public void setAccelSerial(String accelSerial) {
            this.accelSerial = accelSerial;
        }

        public String getAccelTotMem() {
            return accelTotMem;
        }

        public void setAccelTotMem(String accelTotMem) {
            this.accelTotMem = accelTotMem;
        }

        public int getHasAccel() {
            return hasAccel;
        }

        public void setHasAccel(int hasAccel) {
            this.hasAccel = hasAccel;
        }

        public int getHasEncrypt() {
            return hasEncrypt;
        }

        public void setHasEncrypt(int hasEncrypt) {
            this.hasEncrypt = hasEncrypt;
        }

        public List<LogicalDrivesBean> getLogicalDrives() {
            return logicalDrives;
        }

        public void setLogicalDrives(List<LogicalDrivesBean> logicalDrives) {
            this.logicalDrives = logicalDrives;
        }

        public List<EnclosuresBean> getEnclosures() {
            return enclosures;
        }

        public void setEnclosures(List<EnclosuresBean> enclosures) {
            this.enclosures = enclosures;
        }

        public static class LogicalDrivesBean {
            /**
             * name : Logical Drive 01
             * status : OP_STATUS_OK
             * capacity : 1676 GB
             * log_status : LOG_OK
             * flt_tol : RAID 5
             * lu_cache_flag : LOG_CACHE_FLAG_NONMEMBER
             * lu_cache_LUN : HIDDEN
             * encr_stat : ENCR_NOT_ENCR
             * physical_drives : [0,1,2,3]
             */

            @SerializedName("name")
            private String name;
            @SerializedName("status")
            private String status;
            @SerializedName("capacity")
            private String capacity;
            @SerializedName("log_status")
            private String logStatus;
            @SerializedName("flt_tol")
            private String fltTol;
            @SerializedName("lu_cache_flag")
            private String luCacheFlag;
            @SerializedName("lu_cache_LUN")
            private String luCacheLUN;
            @SerializedName("encr_stat")
            private String encrStat;
            @SerializedName("physical_drives")
            private List<Integer> physicalDrives;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCapacity() {
                return capacity;
            }

            public void setCapacity(String capacity) {
                this.capacity = capacity;
            }

            public String getLogStatus() {
                return logStatus;
            }

            public void setLogStatus(String logStatus) {
                this.logStatus = logStatus;
            }

            public String getFltTol() {
                return fltTol;
            }

            public void setFltTol(String fltTol) {
                this.fltTol = fltTol;
            }

            public String getLuCacheFlag() {
                return luCacheFlag;
            }

            public void setLuCacheFlag(String luCacheFlag) {
                this.luCacheFlag = luCacheFlag;
            }

            public String getLuCacheLUN() {
                return luCacheLUN;
            }

            public void setLuCacheLUN(String luCacheLUN) {
                this.luCacheLUN = luCacheLUN;
            }

            public String getEncrStat() {
                return encrStat;
            }

            public void setEncrStat(String encrStat) {
                this.encrStat = encrStat;
            }

            public List<Integer> getPhysicalDrives() {
                return physicalDrives;
            }

            public void setPhysicalDrives(List<Integer> physicalDrives) {
                this.physicalDrives = physicalDrives;
            }
        }

        public static class EnclosuresBean {
            /**
             * name : Drive Enclosure Port 1I Box 2
             * status : OP_STATUS_OK
             * ports : 4
             */

            @SerializedName("name")
            private String name;
            @SerializedName("status")
            private String status;
            @SerializedName("ports")
            private String ports;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getPorts() {
                return ports;
            }

            public void setPorts(String ports) {
                this.ports = ports;
            }
        }
    }
}
