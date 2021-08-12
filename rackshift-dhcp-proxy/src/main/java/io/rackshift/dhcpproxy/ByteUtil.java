package io.rackshift.dhcpproxy;

public class ByteUtil {

    public static int readUInt8(byte[] data, int skips) {
        if (skips < data.length)
            return getUInt(data[skips]);
        return 0;
    }

    public static int readUInt16(byte[] data, int skips) {
        if (skips < data.length)
            return new Integer(getUInt(data[skips]) + getUInt(data[skips + 1]));
        return 0;
    }

    public static int readUInt32(byte[] data, int skips) {
        if (skips < data.length)
            return new Integer(getUInt(data[skips]) + getUInt(data[skips + 1]) + getUInt(data[skips + 2]) + getUInt(data[skips + 3]));
        return 0;
    }

    public static String readIp(byte[] data, int skips) {
        if (skips < data.length)
            return getUInt(data[skips]) + "." + getUInt(data[skips + 1]) + "." + getUInt(data[skips + 2]) + "." + getUInt(data[skips + 3]);
        return null;
    }

    public static String readMAC(byte[] data, int skips, int hlen) {
        if (skips < data.length) {
            String s = "", t = "";
            while (hlen > 0) {
                t = Integer.toHexString(data[skips] & 0xFF + 0x100);
                s += t.substring(t.length() - 2, t.length());
                skips++;
                hlen--;
                if (hlen > 0) {
                    s += ":";
                }
            }
            return s;
        }
        return null;
    }

    private static int getUInt(byte origin) {
        return origin & 0xFF;
    }

    public static void main(String[] args) {
        System.out.println(Integer.toHexString(149));
    }
}
