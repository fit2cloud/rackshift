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

            throw new RuntimeException("Invalid Message Type value: " + code);
        }

    }

    public enum DHCPMessageType {
        DHCPDISCOVER(1), DHCPOFFER(2),
        DHCPREQUEST(3), DHCPDECLINE(4),
        DHCPACK(5), DHCPNAK(6),
        DHCPRELEASE(7), DHCPINFORM(8);
        int code;

        DHCPMessageType(int code) {
            this.code = code;
        }

        static DHCPMessageType getByCode(int code) {
            for (DHCPMessageType t : values()) {
                if (t.code == code) {
                    return t;
                }
            }

            throw new RuntimeException("Invalid DHCP Message Type value: " + code);
        }

    }
}
