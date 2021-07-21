package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.BareMetalExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BareMetalMapper {
    long countByExample(BareMetalExample example);

    int deleteByExample(BareMetalExample example);

    int deleteByPrimaryKey(String id);

    int insert(BareMetal record);

    int insertSelective(BareMetal record);

    List<BareMetal> selectByExample(BareMetalExample example);

    BareMetal selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") BareMetal record, @Param("example") BareMetalExample example);

    int updateByExample(@Param("record") BareMetal record, @Param("example") BareMetalExample example);

    int updateByPrimaryKeySelective(BareMetal record);

    int updateByPrimaryKey(BareMetal record);
}