package io.rackshift.constants;

public class ServiceConstants {
    public static final String SYSTEM = "system";
    public static final String ENABLE = "enable";
    public static final String DISABLE = "disable";
    public static final String ENDPOINT = "endpoint";

    public enum EndPointType {
        main_endpoint("主节点", "main_endpoint"), slave_endpoint("从节点", "slave_endpoint");

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
        PENDING, ERROR, SUCCESS
    }

    public enum ImageStatusEnum {
        not_detected, detected, error
    }

    public enum TaskStatusEnum {
        created, running, failed, succeeded
    }
}
