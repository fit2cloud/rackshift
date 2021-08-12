package io.rackshift.dhcpproxy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class DHCPPacketParser {

    public static JSONObject parse(ByteBuf byteBuf) {
        byte[] dataByte = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(dataByte);
        JSONObject packet = new JSONObject();
        int code = 0;
        int offeset = 240;
        JSONArray unhandledOptions = new JSONArray();

        packet.put("op", DHCPProtocolConstants.MessageType.getByCode(Integer.valueOf(dataByte[0])));
        packet.put("hlen", ByteUtil.readUInt8(dataByte, 2));
        packet.put("hops", ByteUtil.readUInt8(dataByte, 3));
        packet.put("xid", ByteUtil.readUInt32(dataByte, 4));
        packet.put("secs", ByteUtil.readUInt16(dataByte, 8));
        packet.put("flags", ByteUtil.readUInt16(dataByte, 10));
        packet.put("ciaddr", ByteUtil.readIp(dataByte, 12));
        packet.put("yiaddr", ByteUtil.readIp(dataByte, 16));
        packet.put("siaddr", ByteUtil.readIp(dataByte, 20));
        packet.put("giaddr", ByteUtil.readIp(dataByte, 24));
        packet.put("chaddr", ByteUtil.readMAC(dataByte, 28, (Integer) packet.get("hlen")));
        packet.put("sname", byteBuf.toString(44, 64, Charset.forName("ascii")));
        packet.put("fname", byteBuf.toString(108, 128, Charset.forName("ascii")));
//
//        while (code != 255 && dataByte.length > offeset) {
////            code = dataByte[offeset]
//
//
//        }


        return packet;
    }
}
