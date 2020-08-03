package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.Ip;
import io.rackshift.mybatis.domain.IpExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IpMapper {
    long countByExample(IpExample example);

    int deleteByExample(IpExample example);

    int deleteByPrimaryKey(String id);

    int insert(Ip record);

    int insertSelective(Ip record);

    List<Ip> selectByExample(IpExample example);

    Ip selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Ip record, @Param("example") IpExample example);

    int updateByExample(@Param("record") Ip record, @Param("example") IpExample example);

    int updateByPrimaryKeySelective(Ip record);

    int updateByPrimaryKey(Ip record);
}