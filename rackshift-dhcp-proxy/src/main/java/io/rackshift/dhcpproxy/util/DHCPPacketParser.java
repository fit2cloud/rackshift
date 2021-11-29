package io.rackshift.dhcpproxy.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.rackshift.dhcpproxy.constants.ConfigConstants;
import io.rackshift.dhcpproxy.constants.DHCPProtocolConstants;
import org.apache.commons.lang.StringUtils;

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
        packet.put("xid", ByteUtil.readUInt32String(dataByte, 4));
        packet.put("secs", ByteUtil.readUInt16String(dataByte, 8));
        packet.put("flags", ByteUtil.readUInt16String(dataByte, 10));
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
                    options.put("timeOffset", ByteUtil.readUInt32String(dataByte, offset));
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
                        String time = ByteUtil.readUInt32String(dataByte, offset);
                        timeServerOption.add(time);
                        offset += 4;
                    }
                    options.put("timeServerOption", timeServerOption);
                    break;

                case 5:
                    JSONArray nameServerOption = new JSONArray();
                    for (int i = 0; i < len; i++) {
                        String name = ByteUtil.readUInt32String(dataByte, offset);
                        nameServerOption.add(name);
                        offset += 4;
                    }
                    options.put("nameServerOption", nameServerOption);
                    break;

                case 6:
                    JSONArray domainServerOption = new JSONArray();
                    for (int i = 0; i < len; i++) {
                        String name = ByteUtil.readUInt32String(dataByte, offset);
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
                    options.put("ipAddressLeaseTime", ByteUtil.readUInt32String(dataByte, offset));
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
                    options.put("bootFileName", ByteUtil.readString(byteBuf, dataByte, offset));
                    offset += len;
                    break;

                case 77:
                    options.put("userClass", ByteUtil.readString(byteBuf, dataByte, offset));
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


    public static JSONObject createDHCPPROXYAck(JSONObject dhcpPackets, String bootFileName) {

        JSONObject packet = (JSONObject) dhcpPackets.clone();
        boolean isPXEefi = false;

        packet.put("op", DHCPProtocolConstants.MessageType.BOOTREPLY.getCode());
        packet.put("fname", bootFileName);

        // Necessary, at least on vbox
        //support multiple net segment
        String ciaddr = packet.getString("ciaddr");
        String[] nextServers = ConfigurationUtil.getConfigs(ConfigConstants.TFTP_URL, "172.31.128.1");
        for (String nextServer : nextServers) {
            if (IPUtils.isInRange(ciaddr, nextServer + "/24")) {
                packet.put("siaddr", nextServer);
                // Not necessary, at least on vbox, but perhaps other clients will require these fields?
                packet.put("sname", nextServer);
            }
        }

        //EFI PXE listen on a different port => tell the server
        if ((dhcpPackets.getJSONObject("options").getString("userClass") == null) &&
                (dhcpPackets.getJSONObject("options").getInteger("archType") == 6 || // EFI32
                        dhcpPackets.getJSONObject("options").getInteger("archType") == 7 || dhcpPackets.getJSONObject("options").getInteger("archType") == 9)) { // EFIx64
            isPXEefi = true;
        }
        packet.put("options", new JSONObject());
        //EFI pxe use option 67 for bootfilename.
        //Since option 67 doesn't include a field for string length,
        //bootfilname needs to be null-terminated
        packet.getJSONObject("options").put("bootFileName", bootFileName);

        // DHCP MESSAGE TYPES
        packet.getJSONObject("options").put("dhcpMessageType",
                DHCPProtocolConstants.DHCPMessageType.DHCPACK.getCode());

        return packet;
    }

    public static ByteBuf createDHCPPROXYAckBuffer(JSONObject packet) {
        ByteBuf byteBuf = ByteBufUtil.threadLocalDirectBuffer();

        int hlen = packet.getInteger("hlen");
        int hops = packet.getInteger("hops");
        String xid = packet.getString("xid");
        String secs = packet.getString("secs");
        String flags = packet.getString("flags");

        String ciaddr = packet.getString("ciaddr");
        String yiaddr = packet.getString("yiaddr");
        String siaddr = packet.getString("siaddr");
        String giaddr = packet.getString("giaddr");
        //macaddress
        String chaddr = packet.getString("chaddr");
        String sname = packet.getString("sname");
        String fname = packet.getString("fname");

        //op
        byteBuf.writeByte(DHCPProtocolConstants.MessageType.BOOTREPLY.getCode());
        byteBuf.writeByte(1);
        byteBuf.writeByte(hlen);
        byteBuf.writeByte(hops);

        if (StringUtils.isNotBlank(xid)) {
            for (String s : xid.split("-")) {
                byteBuf.writeByte(Integer.parseInt(s, 16));
            }
        }


        if (StringUtils.isNotBlank(secs)) {
            for (String s : secs.split("-")) {
                byteBuf.writeByte(Integer.parseInt(s, 16));
            }
        }

        if (StringUtils.isNotBlank(flags)) {
            for (String s : flags.split("-")) {
                byteBuf.writeByte(Integer.parseInt(s, 16));
            }
        }

        for (String s : ciaddr.split("\\.")) {
            byteBuf.writeByte(Integer.valueOf(s));
        }

        for (String s : yiaddr.split("\\.")) {
            byteBuf.writeByte(Integer.valueOf(s));
        }

        for (String s : siaddr.split("\\.")) {
            byteBuf.writeByte(Integer.valueOf(s));
        }

        for (String s : giaddr.split("\\.")) {
            byteBuf.writeByte(Integer.valueOf(s));
        }

        for (String s : chaddr.split(":")) {
            byteBuf.writeByte(Integer.parseInt(s, 16));
        }
        int length = 16 - chaddr.split(":").length;
        byteBuf.writeZero(length);

        length = 64 - sname.length();
        for (byte aByte : sname.getBytes()) {
            byteBuf.writeByte(aByte);
        }
        byteBuf.writeZero(length);

        length = 128 - fname.length();
        for (byte aByte : fname.getBytes()) {
            byteBuf.writeByte(aByte);
        }
        byteBuf.writeZero(length);
        //Magic Cookie
        byteBuf.writeInt(Integer.parseInt("63825363", 16));

        JSONObject options = packet.getJSONObject("options");
        //option1 Subnet Mask
        String subnetMask = options.getString("subnetMask");
        byteBuf.writeByte(1);
        byteBuf.writeByte(4);
        if (StringUtils.isNotBlank(subnetMask)) {
            for (String s : subnetMask.split("\\.")) {
                byteBuf.writeByte(Integer.valueOf(s));
            }
        } else {
            byteBuf.writeZero(4);
        }

        //option2 Time Offset
        String timeOffset = options.getString("timeOffset");
        byteBuf.writeByte(2);
        byteBuf.writeByte(4);
        if (StringUtils.isNotBlank(timeOffset)) {
            for (String s : timeOffset.split("-")) {
                byteBuf.writeByte(Integer.parseInt(s, 16));
            }
        } else {
            byteBuf.writeZero(4);
        }

        //option3
        JSONArray routerOptions = options.getJSONArray("routerOptions");
        if (routerOptions != null && routerOptions.size() > 0) {
            byteBuf.writeByte(3);
            byteBuf.writeByte(routerOptions.size());
            for (int i = 0; i < routerOptions.size(); i++) {
                for (String s : routerOptions.getString(i).split("\\.")) {
                    byteBuf.writeByte(Integer.valueOf(s));
                }
            }
        }

        //option4
        JSONArray timeServerOption = options.getJSONArray("timeServerOption");
        if (timeServerOption != null && timeServerOption.size() > 0) {
            byteBuf.writeByte(4);
            byteBuf.writeByte(timeServerOption.size());
            for (int i = 0; i < timeServerOption.size(); i++) {
                for (String s : timeServerOption.getString(i).split("-")) {
                    byteBuf.writeByte(Integer.parseInt(s, 16));
                }
            }
        }

        //option5
        JSONArray nameServerOption = options.getJSONArray("nameServerOption");
        if (nameServerOption != null && nameServerOption.size() > 0) {
            byteBuf.writeByte(5);
            byteBuf.writeByte(nameServerOption.size());
            for (int i = 0; i < nameServerOption.size(); i++) {
                for (String s : nameServerOption.getString(i).split("-")) {
                    byteBuf.writeByte(Integer.parseInt(s, 16));
                }
            }
        }

        //option6
        JSONArray domainServerOption = options.getJSONArray("domainServerOption");
        if (domainServerOption != null && domainServerOption.size() > 0) {
            byteBuf.writeByte(6);
            byteBuf.writeByte(domainServerOption.size());
            for (int i = 0; i < domainServerOption.size(); i++) {
                for (String s : domainServerOption.getString(i).split("-")) {
                    byteBuf.writeByte(Integer.parseInt(s, 16));
                }
            }
        }

        //option12
        String hostName = options.getString("hostName");
        if (StringUtils.isNotBlank(hostName)) {
            byteBuf.writeByte(12);
            byteBuf.writeByte(hostName.length());
            byteBuf.writeBytes(hostName.getBytes());
        }
        //option15
        String domainName = options.getString("domainName");
        if (StringUtils.isNotBlank(domainName)) {
            byteBuf.writeByte(15);
            byteBuf.writeByte(domainName.length());
            byteBuf.writeBytes(domainName.getBytes());
        }

        //option28
        String broadcastAddress = options.getString("broadcastAddress");
        if (StringUtils.isNotBlank(broadcastAddress)) {
            byteBuf.writeByte(28);
            byteBuf.writeByte(4);
            for (String s : broadcastAddress.split("\\.")) {
                byteBuf.writeByte(Integer.valueOf(s));
            }
        }

        //option43
        JSONArray vendorOptions = options.getJSONArray("vendorOptions");
        if (vendorOptions != null && vendorOptions.size() > 0) {
            byteBuf.writeByte(43);
            byteBuf.writeByte(vendorOptions.size());
            for (int i = 0; i < vendorOptions.size(); i++) {
                byteBuf.writeByte(vendorOptions.getJSONObject(i).getInteger("vendorOpt"));
                byteBuf.writeByte(vendorOptions.getJSONObject(i).getString("optionContent").length());
                byteBuf.writeBytes(vendorOptions.getJSONObject(i).getString("optionContent").getBytes());
            }
        }

        //option50
        String requestedIpAddress = options.getString("requestedIpAddress");
        if (StringUtils.isNotBlank(requestedIpAddress)) {
            byteBuf.writeByte(50);
            byteBuf.writeByte(4);
            for (String s : requestedIpAddress.split("\\.")) {
                byteBuf.writeByte(Integer.valueOf(s));
            }
        }

        //option51
        Integer ipAddressLeaseTime = options.getInteger("ipAddressLeaseTime");
        if (ipAddressLeaseTime != null) {
            byteBuf.writeByte(51);
            byteBuf.writeByte(4);
            byteBuf.writeInt(ipAddressLeaseTime);
        }

        //option52
        Integer optionOverload = options.getInteger("optionOverload");
        if (optionOverload != null) {
            byteBuf.writeByte(52);
            byteBuf.writeByte(1);
            byteBuf.writeByte(optionOverload);
        }

        //option53
        byteBuf.writeByte(53);
        byteBuf.writeByte(1);
        byteBuf.writeByte(DHCPProtocolConstants.DHCPMessageType.DHCPACK.getCode());

        //option54
        String serverIdentifier = options.getString("serverIdentifier");
        if (StringUtils.isNotBlank(serverIdentifier)) {
            byteBuf.writeByte(54);
            byteBuf.writeByte(4);
            for (String s : serverIdentifier.split("\\.")) {
                byteBuf.writeByte(Integer.valueOf(s));
            }
        }

        //option60
        String vendorClassIdentifier = options.getString("vendorClassIdentifier");
        if (StringUtils.isNotBlank(vendorClassIdentifier)) {
            byteBuf.writeByte(60);
            byteBuf.writeByte(vendorClassIdentifier.length());
            byteBuf.writeBytes(vendorClassIdentifier.getBytes());
        }

        //option61
        String clientIdentifier = options.getString("clientIdentifier");
        if (StringUtils.isNotBlank(clientIdentifier)) {
            byteBuf.writeByte(60);
            byteBuf.writeByte(clientIdentifier.split(":").length);
            for (String s : clientIdentifier.split(":")) {
                byteBuf.writeByte(Integer.parseInt(s, 16));
            }
        }

        //option67
        String bootFileName = options.getString("bootFileName");
        byteBuf.writeByte(67);
        byteBuf.writeByte(bootFileName.length());
        byteBuf.writeBytes(bootFileName.getBytes());

        //option77
        String userClass = options.getString("userClass");
        if (StringUtils.isNotBlank(userClass)) {
            byteBuf.writeByte(77);
            byteBuf.writeByte(userClass.length());
            byteBuf.writeBytes(userClass.getBytes());
        }

        //option93
        Integer archType = options.getInteger("archType");
        if (archType != null) {
            byteBuf.writeByte(93);
            byteBuf.writeByte(2);
            byteBuf.writeShort(archType);
        }

        //option255 结束
        byteBuf.writeByte(255);
        byteBuf.writeByte(1);

        //padding
        if (byteBuf.writableBytes() % 2 > 0) {
            byteBuf.writeZero(1);
        } else {
            byteBuf.writeZero(2);
        }

        int remain = 300 - byteBuf.writableBytes();
        if (remain > 0) {
            byteBuf.writeZero(remain);
        }
        return byteBuf;
    }
}