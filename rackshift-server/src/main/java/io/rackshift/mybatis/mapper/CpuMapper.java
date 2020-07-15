package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.Cpu;
import io.rackshift.mybatis.domain.CpuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CpuMapper {
    long countByExample(CpuExample example);

    int deleteByExample(CpuExample example);

    int deleteByPrimaryKey(String id);

    int insert(Cpu record);

    int insertSelective(Cpu record);

    List<Cpu> selectByExample(CpuExample example);

    Cpu selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Cpu record, @Param("example") CpuExample example);

    int updateByExample(@Param("record") Cpu record, @Param("example") CpuExample example);

    int updateByPrimaryKeySelective(Cpu record);

    int updateByPrimaryKey(Cpu record);
}