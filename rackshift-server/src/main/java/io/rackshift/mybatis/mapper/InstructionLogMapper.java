package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.InstructionLog;
import io.rackshift.mybatis.domain.InstructionLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface InstructionLogMapper {
    long countByExample(InstructionLogExample example);

    int deleteByExample(InstructionLogExample example);

    int deleteByPrimaryKey(String id);

    int insert(InstructionLog record);

    int insertSelective(InstructionLog record);

    List<InstructionLog> selectByExampleWithBLOBs(InstructionLogExample example);

    List<InstructionLog> selectByExample(InstructionLogExample example);

    InstructionLog selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") InstructionLog record, @Param("example") InstructionLogExample example);

    int updateByExampleWithBLOBs(@Param("record") InstructionLog record, @Param("example") InstructionLogExample example);

    int updateByExample(@Param("record") InstructionLog record, @Param("example") InstructionLogExample example);

    int updateByPrimaryKeySelective(InstructionLog record);

    int updateByPrimaryKeyWithBLOBs(InstructionLog record);

    int updateByPrimaryKey(InstructionLog record);
}