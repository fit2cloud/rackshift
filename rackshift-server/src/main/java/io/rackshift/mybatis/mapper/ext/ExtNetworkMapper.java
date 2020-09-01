package io.rackshift.mybatis.mapper.ext;

import io.rackshift.mybatis.domain.Network;

import java.util.List;

public interface ExtNetworkMapper {
    List<Network> getByType(Network param);
}