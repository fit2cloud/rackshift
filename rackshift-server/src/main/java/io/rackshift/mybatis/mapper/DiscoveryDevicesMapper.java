package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.DiscoveryDevices;
import io.rackshift.mybatis.domain.DiscoveryDevicesExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DiscoveryDevicesMapper {
    long countByExample(DiscoveryDevicesExample example);

    int deleteByExample(DiscoveryDevicesExample example);

    int deleteByPrimaryKey(String id);

    int insert(DiscoveryDevices record);

    int insertSelective(DiscoveryDevices record);

    List<DiscoveryDevices> selectByExample(DiscoveryDevicesExample example);

    DiscoveryDevices selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") DiscoveryDevices record, @Param("example") DiscoveryDevicesExample example);

    int updateByExample(@Param("record") DiscoveryDevices record, @Param("example") DiscoveryDevicesExample example);

    int updateByPrimaryKeySelective(DiscoveryDevices record);

    int updateByPrimaryKey(DiscoveryDevices record);
}