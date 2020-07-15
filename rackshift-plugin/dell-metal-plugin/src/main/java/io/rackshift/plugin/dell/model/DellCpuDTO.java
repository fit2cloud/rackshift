package io.rackshift.plugin.dell.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DellCpuDTO {

    /**
     * brand : Intel(R) Xeon(R) CPU E5-2670 0 @ 2.60GHz
     * cache : /sysmgmt/2012/server/cache?processor=D2||CPU.Socket.1
     * core_count : 8
     * current_speed : 2600
     * executeDisable : [{"capable":1,"enabled":1}]
     * hyperThreading : [{"capable":1,"enabled":1}]
     * name : [CPU1]
     * state : 3
     * status : 2
     * turboMode : [{"capable":1,"enabled":1}]
     * version : E5
     * virtualizationTech : [{"capable":1,"enabled":1}]
     */

    @SerializedName("brand")
    private String brand;
    @SerializedName("cache")
    private String cache;
    @SerializedName("core_count")
    private int coreCount;
    @SerializedName("current_speed")
    private int currentSpeed;
    @SerializedName("name")
    private String name;
    @SerializedName("state")
    private int state;
    @SerializedName("status")
    private int status;
    @SerializedName("version")
    private String version;
    @SerializedName("executeDisable")
    private List<ExecuteDisableBean> executeDisable;
    @SerializedName("hyperThreading")
    private List<HyperThreadingBean> hyperThreading;
    @SerializedName("turboMode")
    private List<TurboModeBean> turboMode;
    @SerializedName("virtualizationTech")
    private List<VirtualizationTechBean> virtualizationTech;
    private String socket;

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    public int getCoreCount() {
        return coreCount;
    }

    public void setCoreCount(int coreCount) {
        this.coreCount = coreCount;
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<ExecuteDisableBean> getExecuteDisable() {
        return executeDisable;
    }

    public void setExecuteDisable(List<ExecuteDisableBean> executeDisable) {
        this.executeDisable = executeDisable;
    }

    public List<HyperThreadingBean> getHyperThreading() {
        return hyperThreading;
    }

    public void setHyperThreading(List<HyperThreadingBean> hyperThreading) {
        this.hyperThreading = hyperThreading;
    }

    public List<TurboModeBean> getTurboMode() {
        return turboMode;
    }

    public void setTurboMode(List<TurboModeBean> turboMode) {
        this.turboMode = turboMode;
    }

    public List<VirtualizationTechBean> getVirtualizationTech() {
        return virtualizationTech;
    }

    public void setVirtualizationTech(List<VirtualizationTechBean> virtualizationTech) {
        this.virtualizationTech = virtualizationTech;
    }

    public static class ExecuteDisableBean {
        /**
         * capable : 1
         * enabled : 1
         */

        @SerializedName("capable")
        private int capable;
        @SerializedName("enabled")
        private int enabled;

        public int getCapable() {
            return capable;
        }

        public void setCapable(int capable) {
            this.capable = capable;
        }

        public int getEnabled() {
            return enabled;
        }

        public void setEnabled(int enabled) {
            this.enabled = enabled;
        }
    }

    public static class HyperThreadingBean {
        /**
         * capable : 1
         * enabled : 1
         */

        @SerializedName("capable")
        private int capable;
        @SerializedName("enabled")
        private int enabled;

        public int getCapable() {
            return capable;
        }

        public void setCapable(int capable) {
            this.capable = capable;
        }

        public int getEnabled() {
            return enabled;
        }

        public void setEnabled(int enabled) {
            this.enabled = enabled;
        }
    }

    public static class TurboModeBean {
        /**
         * capable : 1
         * enabled : 1
         */

        @SerializedName("capable")
        private int capable;
        @SerializedName("enabled")
        private int enabled;

        public int getCapable() {
            return capable;
        }

        public void setCapable(int capable) {
            this.capable = capable;
        }

        public int getEnabled() {
            return enabled;
        }

        public void setEnabled(int enabled) {
            this.enabled = enabled;
        }
    }

    public static class VirtualizationTechBean {
        /**
         * capable : 1
         * enabled : 1
         */

        @SerializedName("capable")
        private int capable;
        @SerializedName("enabled")
        private int enabled;

        public int getCapable() {
            return capable;
        }

        public void setCapable(int capable) {
            this.capable = capable;
        }

        public int getEnabled() {
            return enabled;
        }

        public void setEnabled(int enabled) {
            this.enabled = enabled;
        }
    }
}
