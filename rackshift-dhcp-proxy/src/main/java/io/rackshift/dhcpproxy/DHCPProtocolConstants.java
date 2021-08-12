package io.rackshift.dhcpproxy;

public class DHCPProtocolConstants {
    public enum MessageType {
        BOOTREQUEST(1), BOOTREPLY(2);
        int code;

        MessageType(int code) {
            this.code = code;
        }

        static MessageType getByCode(int code) {
            for (MessageType t : values()) {
                if (t.code == code) {
                    return t;
                }
            }

            throw new RuntimeException("Invalid DHCP Message Type value: " + code);
        }

    }
}
