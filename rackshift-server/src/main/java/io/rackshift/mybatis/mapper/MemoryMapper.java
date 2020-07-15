package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.Memory;
import io.rackshift.mybatis.domain.MemoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MemoryMapper {
    long countByExample(MemoryExample example);

    int deleteByExample(MemoryExample example);

    int deleteByPrimaryKey(String id);

    int insert(Memory record);

    int insertSelective(Memory record);

    List<Memory> selectByExample(MemoryExample example);

    Memory selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Memory record, @Param("example") MemoryExample example);

    int updateByExample(@Param("record") Memory record, @Param("example") MemoryExample example);

    int updateByPrimaryKeySelective(Memory record);

    int updateByPrimaryKey(Memory record);
}