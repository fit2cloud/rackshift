package io.rackshift.dhcpproxy.util;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class ByteUtil {

    public static int readUInt8(byte[] data, int skips) {
        if (skips < data.length)
            return getUInt(data[skips]);
        return 0;
    }

    public static int readUInt16(byte[] data, int skips) {
        if (skips < data.length)
            return Integer.parseInt(Integer.toHexString(getUInt(data[skips])) + Integer.toHexString(getUInt(data[skips + 1])), 16);
        return 0;
    }

    public static String readUInt32String(byte[] data, int skips) {
        if (skips < data.length)
            return Integer.toHexString(getUInt(data[skips])) + "-" + Integer.toHexString(getUInt(data[skips + 1])) + "-" + Integer.toHexString(getUInt(data[skips + 2])) + "-" + Integer.toHexString(getUInt(data[skips + 3]));
        return "";
    }

    public static String readIp(byte[] data, int skips) {
        if (skips < data.length)
            return getUInt(data[skips]) + "." + getUInt(data[skips + 1]) + "." + getUInt(data[skips + 2]) + "." + getUInt(data[skips + 3]);
        return null;
    }

    public static String readString(ByteBuf byteBuf, byte[] bytes, int offset) {
        int len = readUInt8(bytes, offset - 1);
        return byteBuf.toString(offset, len, Charset.forName("ascii")).trim();
    }

    public static String readMAC(byte[] data, int skips, int hlen) {
        if (skips < data.length) {
            String s = "", t = "";
            while (hlen > 0) {
                t = Integer.toHexString(data[skips] & 0xFF + 0x100);
                if (t.length() == 1) {
                    s += "0" + t;
                } else {
                    s += t.substring(t.length() - 2, t.length());
                }
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

}
