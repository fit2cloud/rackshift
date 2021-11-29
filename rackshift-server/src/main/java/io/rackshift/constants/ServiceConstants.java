package io.rackshift.constants;

public class ServiceConstants {
    public static final String SYSTEM = "system";
    public static final String ENABLE = "enable";
    public static final String DISABLE = "disable";
    public static final String ENDPOINT = "endpoint";

    public static final String IPMI_Rest = "IPMI+Rest";
    public static final String SNMP = "SNMP";
    public static final String TYPE_SYS = "system";
    public static final String TYPE_USER = "user";
    public static final String PARAM_ALL_BRANDS = "brands";


    public enum EndPointType {
        slave_endpoint("从节点", "slave_endpoint"), main_endpoint("主节点", "main_endpoint");

        private String name;
        private String value;

        EndPointType(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {
            return "{" +
                    "'name' : '" + name + '\'' +
                    ", 'value' : '" + value + '\'' +
                    '}';
        }

        public String value() {
            return this.value;
        }
    }

    public enum EndpointStatusEnum {
        Online, Offline
    }


    public enum DiscoveryStatusEnum {
        PENDING, COMPLETE
    }

    public enum ImageStatusEnum {
        not_detected, detected, error
    }

    public enum TaskStatusEnum {
        created, running, failed, cancelled, succeeded, finished
    }

    public enum RackHDTaskStatusEnum {
        pending, failed, cancelled, timeout, succeeded, finished
    }
}
