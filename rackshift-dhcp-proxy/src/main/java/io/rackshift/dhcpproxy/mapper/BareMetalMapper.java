package io.rackshift.dhcpproxy.mapper;

import io.rackshift.dhcpproxy.model.BareMetal;

public interface BareMetalMapper {
    BareMetal findByMac(String pxeMac);
}
