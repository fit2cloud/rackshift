package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.ExecutionLogDetails;
import io.rackshift.mybatis.domain.ExecutionLogDetailsExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExecutionLogDetailsMapper {
    long countByExample(ExecutionLogDetailsExample example);

    int deleteByExample(ExecutionLogDetailsExample example);

    int deleteByPrimaryKey(String id);

    int insert(ExecutionLogDetails record);

    int insertSelective(ExecutionLogDetails record);

    List<ExecutionLogDetails> selectByExampleWithBLOBs(ExecutionLogDetailsExample example);

    List<ExecutionLogDetails> selectByExample(ExecutionLogDetailsExample example);

    ExecutionLogDetails selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ExecutionLogDetails record, @Param("example") ExecutionLogDetailsExample example);

    int updateByExampleWithBLOBs(@Param("record") ExecutionLogDetails record, @Param("example") ExecutionLogDetailsExample example);

    int updateByExample(@Param("record") ExecutionLogDetails record, @Param("example") ExecutionLogDetailsExample example);

    int updateByPrimaryKeySelective(ExecutionLogDetails record);

    int updateByPrimaryKeyWithBLOBs(ExecutionLogDetails record);

    int updateByPrimaryKey(ExecutionLogDetails record);
}