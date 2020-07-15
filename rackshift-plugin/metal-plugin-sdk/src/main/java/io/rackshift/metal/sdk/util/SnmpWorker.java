package io.rackshift.metal.sdk.util;

import io.rackshift.metal.sdk.MetalPluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

public class SnmpWorker implements Closeable {
    private Logger logger = LoggerFactory.getLogger(SnmpWorker.class);

    private String address;
    private int port;
    private Snmp snmp = null;
    private int version = SnmpConstants.version2c;
    private int retry = 2;
    private long timeout = 1500;
    private String community;
    private Target target = null;

    public SnmpWorker(String address, String community, int port) throws IOException {
        this.address = address;
        this.community = community;
        this.port = port;
        init();
    }

    private void init() throws IOException {
        TransportMapping transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
        target = getTarget();
    }

    private String getAddress() {
        return "udp:" + address + "/" + Integer.toString(port);
    }

    private Target getTarget() {
        Address targetAddress = GenericAddress.parse(getAddress());
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(targetAddress);
        target.setRetries(retry);
        target.setTimeout(timeout);
        target.setVersion(version);
        return target;
    }

    private ResponseEvent get(String... oids) throws IOException, MetalPluginException {
        PDU pdu = new PDU();
        for (String oid : oids) {
            pdu.add(new VariableBinding(new OID(oid)));
        }
        pdu.setType(PDU.GET);
        ResponseEvent event = snmp.send(pdu, target, null);
        if (event != null) {
            return event;
        }
        throw new MetalPluginException(getLogMessage("get timed out"));
    }

    public Map<String, String> walk(String oid) throws MetalPluginException {
        Map<String, String> result = new TreeMap<>();

        TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
        List<TreeEvent> events = treeUtils.getSubtree(target, new OID(oid));
        if (events == null || events.size() == 0) {
            throw new MetalPluginException(getLogMessage("unable to walk " + oid));
        }

        for (TreeEvent event : events) {
            if (event == null) {
                continue;
            }
            if (event.isError()) {
                logger.error(getLogMessage("Error :, OID [" + oid + "] " + event.getErrorMessage()));
                continue;
            }

            VariableBinding[] varBindings = event.getVariableBindings();
            if (varBindings == null || varBindings.length == 0) {
                continue;
            }
            for (VariableBinding varBinding : varBindings) {
                if (varBinding == null) {
                    continue;
                }
                result.put(varBinding.getOid().toString(), varBinding.getVariable().toString());
            }
        }
        return result;
    }

    private List<? extends VariableBinding> getVariables(String... oids) throws IOException, MetalPluginException {
        ResponseEvent responseEvent = get(oids);
        if (responseEvent.getResponse() != null) {
            return responseEvent.getResponse().getVariableBindings();
        } else if (responseEvent.getError() == null && responseEvent.getResponse() == null) {
            throw new MetalPluginException(getLogMessage("fail to get oids" + Arrays.toString(oids) + ", response is null"));
        }
        throw new MetalPluginException(getLogMessage("fail to get oids" + Arrays.toString(oids)), responseEvent.getError());
    }

    public String getAsString(String oid) throws IOException, MetalPluginException {
        return getVariables(oid).get(0).getVariable().toString();
    }

    private String getLogMessage(String message) {
        return "IP:" + address + ", message:" + message;
    }

    @Override
    public void close() throws IOException {
        if (snmp != null) {
            snmp.close();
        }
    }
}
