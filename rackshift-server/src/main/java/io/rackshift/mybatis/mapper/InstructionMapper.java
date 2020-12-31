package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.Instruction;
import io.rackshift.mybatis.domain.InstructionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface InstructionMapper {
    long countByExample(InstructionExample example);

    int deleteByExample(InstructionExample example);

    int deleteByPrimaryKey(String id);

    int insert(Instruction record);

    int insertSelective(Instruction record);

    List<Instruction> selectByExampleWithBLOBs(InstructionExample example);

    List<Instruction> selectByExample(InstructionExample example);

    Instruction selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Instruction record, @Param("example") InstructionExample example);

    int updateByExampleWithBLOBs(@Param("record") Instruction record, @Param("example") InstructionExample example);

    int updateByExample(@Param("record") Instruction record, @Param("example") InstructionExample example);

    int updateByPrimaryKeySelective(Instruction record);

    int updateByPrimaryKeyWithBLOBs(Instruction record);

    int updateByPrimaryKey(Instruction record);
}