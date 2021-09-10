package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.Workflow;
import io.rackshift.mybatis.domain.WorkflowExample;
import io.rackshift.mybatis.domain.WorkflowWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WorkflowMapper {
    long countByExample(WorkflowExample example);

    int deleteByExample(WorkflowExample example);

    int deleteByPrimaryKey(String id);

    int insert(WorkflowWithBLOBs record);

    int insertSelective(WorkflowWithBLOBs record);

    List<WorkflowWithBLOBs> selectByExampleWithBLOBs(WorkflowExample example);

    List<Workflow> selectByExample(WorkflowExample example);

    WorkflowWithBLOBs selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") WorkflowWithBLOBs record, @Param("example") WorkflowExample example);

    int updateByExampleWithBLOBs(@Param("record") WorkflowWithBLOBs record, @Param("example") WorkflowExample example);

    int updateByExample(@Param("record") Workflow record, @Param("example") WorkflowExample example);

    int updateByPrimaryKeySelective(WorkflowWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(WorkflowWithBLOBs record);

    int updateByPrimaryKey(Workflow record);
}