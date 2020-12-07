package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.Network;
import io.rackshift.mybatis.domain.NetworkExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NetworkMapper {
    long countByExample(NetworkExample example);

    int deleteByExample(NetworkExample example);

    int deleteByPrimaryKey(String id);

    int insert(Network record);

    int insertSelective(Network record);

    List<Network> selectByExample(NetworkExample example);

    Network selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Network record, @Param("example") NetworkExample example);

    int updateByExample(@Param("record") Network record, @Param("example") NetworkExample example);

    int updateByPrimaryKeySelective(Network record);

    int updateByPrimaryKey(Network record);
}