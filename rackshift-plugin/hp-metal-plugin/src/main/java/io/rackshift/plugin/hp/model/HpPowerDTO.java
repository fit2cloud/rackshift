package io.rackshift.plugin.hp.model;

// FIXME generate failure  field _$FedcapDEFAULT322
public class HpPowerDTO {

    /**
     * power_capabilities : {"max_cap":1500,"_100_pct_cap":526,"_0_pct_cap":138}
     * power_summary : {"power_supply_capacity":1500,"last_avg_pwr_accum":144}
     * fedcap/DEFAULT : {"contributed_power":0,"throttle":{"low":1},"cap":0,"group_caps":{"DEFAULT":0}}
     * node_count : 1
     * match_count : 1
     * errors : 0
     */

    @com.google.gson.annotations.SerializedName("power_capabilities")
    private PowerCapabilitiesBean powerCapabilities;
    @com.google.gson.annotations.SerializedName("power_summary")
    private PowerSummaryBean powerSummary;
    @com.google.gson.annotations.SerializedName("node_count")
    private int nodeCount;
    @com.google.gson.annotations.SerializedName("match_count")
    private int matchCount;
    @com.google.gson.annotations.SerializedName("errors")
    private int errors;

    public PowerCapabilitiesBean getPowerCapabilities() {
        return powerCapabilities;
    }

    public void setPowerCapabilities(PowerCapabilitiesBean powerCapabilities) {
        this.powerCapabilities = powerCapabilities;
    }

    public PowerSummaryBean getPowerSummary() {
        return powerSummary;
    }

    public void setPowerSummary(PowerSummaryBean powerSummary) {
        this.powerSummary = powerSummary;
    }

    public static class PowerCapabilitiesBean {
        /**
         * max_cap : 1500
         * _100_pct_cap : 526
         * _0_pct_cap : 138
         */

        @com.google.gson.annotations.SerializedName("max_cap")
        private int maxCap;
        @com.google.gson.annotations.SerializedName("_100_pct_cap")
        private int _$100PctCap;
        @com.google.gson.annotations.SerializedName("_0_pct_cap")
        private int _$0PctCap;

        public int getMaxCap() {
            return maxCap;
        }

        public void setMaxCap(int maxCap) {
            this.maxCap = maxCap;
        }

        public int get_$100PctCap() {
            return _$100PctCap;
        }

        public void set_$100PctCap(int _$100PctCap) {
            this._$100PctCap = _$100PctCap;
        }

        public int get_$0PctCap() {
            return _$0PctCap;
        }

        public void set_$0PctCap(int _$0PctCap) {
            this._$0PctCap = _$0PctCap;
        }
    }

    public static class PowerSummaryBean {
        /**
         * power_supply_capacity : 1500
         * last_avg_pwr_accum : 144
         */

        @com.google.gson.annotations.SerializedName("power_supply_capacity")
        private int powerSupplyCapacity;
        @com.google.gson.annotations.SerializedName("last_avg_pwr_accum")
        private int lastAvgPwrAccum;

        public int getPowerSupplyCapacity() {
            return powerSupplyCapacity;
        }

        public void setPowerSupplyCapacity(int powerSupplyCapacity) {
            this.powerSupplyCapacity = powerSupplyCapacity;
        }

        public int getLastAvgPwrAccum() {
            return lastAvgPwrAccum;
        }

        public void setLastAvgPwrAccum(int lastAvgPwrAccum) {
            this.lastAvgPwrAccum = lastAvgPwrAccum;
        }
    }

    public static class Fedcap {

        public static class ThrottleBean {
            /**
             * low : 1
             */

            @com.google.gson.annotations.SerializedName("low")
            private int low;

            public int getLow() {
                return low;
            }

            public void setLow(int low) {
                this.low = low;
            }
        }

        public static class GroupCapsBean {
            /**
             * DEFAULT : 0
             */

            @com.google.gson.annotations.SerializedName("DEFAULT")
            private int DEFAULT;

            public int getDEFAULT() {
                return DEFAULT;
            }

            public void setDEFAULT(int DEFAULT) {
                this.DEFAULT = DEFAULT;
            }
        }

        /**
         * contributed_power : 0
         * throttle : {"low":1}
         * cap : 0
         * group_caps : {"DEFAULT":0}
         */

        @com.google.gson.annotations.SerializedName("contributed_power")
        private int contributedPower;
        @com.google.gson.annotations.SerializedName("throttle")
        private ThrottleBean throttle;
        @com.google.gson.annotations.SerializedName("cap")
        private int cap;
        @com.google.gson.annotations.SerializedName("group_caps")
        private GroupCapsBean groupCaps;

        public int getContributedPower() {
            return contributedPower;
        }

        public void setContributedPower(int contributedPower) {
            this.contributedPower = contributedPower;
        }

        public ThrottleBean getThrottle() {
            return throttle;
        }

        public void setThrottle(ThrottleBean throttle) {
            this.throttle = throttle;
        }

        public int getCap() {
            return cap;
        }

        public void setCap(int cap) {
            this.cap = cap;
        }

        public GroupCapsBean getGroupCaps() {
            return groupCaps;
        }

        public void setGroupCaps(GroupCapsBean groupCaps) {
            this.groupCaps = groupCaps;
        }
    }
}
