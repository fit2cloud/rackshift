package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.Task;
import io.rackshift.mybatis.domain.TaskExample;
import io.rackshift.mybatis.domain.TaskWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskMapper {
    long countByExample(TaskExample example);

    int deleteByExample(TaskExample example);

    int deleteByPrimaryKey(String id);

    int insert(TaskWithBLOBs record);

    int insertSelective(TaskWithBLOBs record);

    List<TaskWithBLOBs> selectByExampleWithBLOBs(TaskExample example);

    List<Task> selectByExample(TaskExample example);

    TaskWithBLOBs selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TaskWithBLOBs record, @Param("example") TaskExample example);

    int updateByExampleWithBLOBs(@Param("record") TaskWithBLOBs record, @Param("example") TaskExample example);

    int updateByExample(@Param("record") Task record, @Param("example") TaskExample example);

    int updateByPrimaryKeySelective(TaskWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(TaskWithBLOBs record);

    int updateByPrimaryKey(Task record);
}