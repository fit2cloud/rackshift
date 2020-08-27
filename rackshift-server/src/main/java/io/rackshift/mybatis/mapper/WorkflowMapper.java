package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.Workflow;
import io.rackshift.mybatis.domain.WorkflowExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WorkflowMapper {
    long countByExample(WorkflowExample example);

    int deleteByExample(WorkflowExample example);

    int deleteByPrimaryKey(String id);

    int insert(Workflow record);

    int insertSelective(Workflow record);

    List<Workflow> selectByExampleWithBLOBs(WorkflowExample example);

    List<Workflow> selectByExample(WorkflowExample example);

    Workflow selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Workflow record, @Param("example") WorkflowExample example);

    int updateByExampleWithBLOBs(@Param("record") Workflow record, @Param("example") WorkflowExample example);

    int updateByExample(@Param("record") Workflow record, @Param("example") WorkflowExample example);

    int updateByPrimaryKeySelective(Workflow record);

    int updateByPrimaryKeyWithBLOBs(Workflow record);

    int updateByPrimaryKey(Workflow record);
}