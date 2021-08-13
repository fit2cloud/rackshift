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
        int len = 0;
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
        packet.put("sname", byteBuf.toString(44, 64, Charset.forName("ascii")).trim());
        packet.put("fname", byteBuf.toString(108, 128, Charset.forName("ascii")).trim());

        //rfc2132 解析 dhcp packet options
        JSONObject options = new JSONObject();

        while (code != 255 && dataByte.length > offset) {
            code = ByteUtil.readUInt8(dataByte, offset);
            offset++;
            len = ByteUtil.readUInt8(dataByte, offset);
            offset++;
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
                    for (int i = 0; i < len; i++) {
                        String ip = ByteUtil.readIp(dataByte, offset);
                        routerOptions.add(ip);
                        offset += 4;
                    }
                    options.put("routerOptions", routerOptions);
                    break;
                case 4:
                    JSONArray timeServerOption = new JSONArray();
                    for (int i = 0; i < len; i++) {
                        int time = ByteUtil.readUInt32(dataByte, offset);
                        timeServerOption.add(time);
                        offset += 4;
                    }
                    options.put("timeServerOption", timeServerOption);
                    break;

                case 5:
                    JSONArray nameServerOption = new JSONArray();
                    for (int i = 0; i < len; i++) {
                        int name = ByteUtil.readUInt32(dataByte, offset);
                        nameServerOption.add(name);
                        offset += 4;
                    }
                    options.put("nameServerOption", nameServerOption);
                    break;

                case 6:
                    JSONArray domainServerOption = new JSONArray();
                    for (int i = 0; i < len; i++) {
                        int name = ByteUtil.readUInt32(dataByte, offset);
                        domainServerOption.add(name);
                        offset += 4;
                    }
                    options.put("domainServerOption", domainServerOption);
                    break;

                case 12:
                    options.put("hostName", ByteUtil.readString(byteBuf, dataByte, offset));
                    offset += len;
                    break;

                case 15:
                    options.put("domainName", ByteUtil.readString(byteBuf, dataByte, offset));
                    offset += len;
                    break;

                case 28:
                    options.put("broadcastAddress", ByteUtil.readString(byteBuf, dataByte, offset));
                    offset += len;
                    break;

                case 43:
                    JSONArray vendorOptions = new JSONArray();
                    for (int i = 0; i < len; i++) {
                        int vendorOpt = ByteUtil.readUInt8(dataByte, offset);
                        offset++;
                        int vendorLen = ByteUtil.readUInt8(dataByte, offset);
                        String optionContent = byteBuf.toString(offset, vendorLen, Charset.forName("ascii"));
                        JSONObject obj = new JSONObject();
                        obj.put("vendorOpt", vendorOpt);
                        obj.put("optionContent", optionContent);
                        vendorOptions.add(obj);
                        offset += vendorLen;
                    }
                    options.put("vendorOptions", vendorOptions);
                    break;

                case 50:
                    options.put("requestedIpAddress", ByteUtil.readIp(dataByte, offset));
                    offset += len;
                    break;

                case 51:
                    options.put("ipAddressLeaseTime", ByteUtil.readUInt32(dataByte, offset));
                    offset += len;
                    break;

                case 52:
                    options.put("optionOverload", ByteUtil.readUInt8(dataByte, offset));
                    offset += len;
                    break;

                case 53:
                    options.put("dhcpMessageType", DHCPProtocolConstants.DHCPMessageType.getByCode(ByteUtil.readUInt8(dataByte, offset)));
                    offset += len;
                    break;

                case 54:
                    options.put("serverIdentifier", ByteUtil.readIp(dataByte, offset));
                    offset += len;
                    break;

                case 60:
                    options.put("vendorClassIdentifier", ByteUtil.readString(byteBuf, dataByte, offset));
                    offset += len;
                    break;

                case 61:
                    offset++;
                    options.put("clientIdentifier", ByteUtil.readMAC(dataByte, offset, len));
                    offset += len;
                    break;

                case 67:
                    options.put("bootFileName", ByteUtil.readString(byteBuf, dataByte, len));
                    offset += len;
                    break;

                case 77:
                    options.put("userClass", ByteUtil.readString(byteBuf, dataByte, len));
                    offset += len;
                    break;

                case 93:
                    options.put("archType", ByteUtil.readUInt16(dataByte, offset));
                    offset += len;
                    break;

                default: {
                    offset += len;
                    break;
                }
            }
        }

        packet.put("options", options);

        return packet;


    }
}