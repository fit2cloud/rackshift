package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.ExecutionLog;
import io.rackshift.mybatis.domain.ExecutionLogExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExecutionLogMapper {
    long countByExample(ExecutionLogExample example);

    int deleteByExample(ExecutionLogExample example);

    int deleteByPrimaryKey(String id);

    int insert(ExecutionLog record);

    int insertSelective(ExecutionLog record);

    List<ExecutionLog> selectByExample(ExecutionLogExample example);

    ExecutionLog selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ExecutionLog record, @Param("example") ExecutionLogExample example);

    int updateByExample(@Param("record") ExecutionLog record, @Param("example") ExecutionLogExample example);

    int updateByPrimaryKeySelective(ExecutionLog record);

    int updateByPrimaryKey(ExecutionLog record);
}