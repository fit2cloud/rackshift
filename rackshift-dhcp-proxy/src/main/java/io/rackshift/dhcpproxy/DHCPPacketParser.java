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
        int offset = 240;
        JSONArray unhandledOptions = new JSONArray();
        //rfc2132 解析 dhcp packet
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

        //rfc2132 解析 dhcp packet options
        JSONObject options = new JSONObject();
        while (code != 255 && dataByte.length > offset) {
            code = ByteUtil.readUInt8(dataByte, offset);
            offset++;
            int len = 0;
            switch (code) {
                case 0:
                    continue;
                case 255:
                    break;
                case 1:
                    options.put("subnetMask", ByteUtil.readIp(dataByte, offset));
                    offset += 4;
                    break;
                case 2:
                    options.put("timeOffset", ByteUtil.readUInt32(dataByte, offset));
                    offset += 4;
                    break;
                case 3:
                    JSONArray routerOptions = new JSONArray();
                    int length = ByteUtil.readUInt8(dataByte, offset);
                    offset++;
                    for (int i = 0; i < length; i++) {
                        String ip = ByteUtil.readIp(dataByte, offset);
                        routerOptions.add(ip);
                        offset += 4;
                    }
                    options.put("routerOptions", routerOptions);
                    break;
                default: {
                    len = ByteUtil.readUInt8(dataByte, offset);
                    offset += 1;
                    offset += len;
                    break;
                }
            }

            packet.put("options", options);

            return packet;
        }
    }
