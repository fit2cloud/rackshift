package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.WorkflowParamTemplates;
import io.rackshift.mybatis.domain.WorkflowParamTemplatesExample;
import io.rackshift.mybatis.domain.WorkflowParamTemplatesWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WorkflowParamTemplatesMapper {
    long countByExample(WorkflowParamTemplatesExample example);

    int deleteByExample(WorkflowParamTemplatesExample example);

    int deleteByPrimaryKey(String id);

    int insert(WorkflowParamTemplatesWithBLOBs record);

    int insertSelective(WorkflowParamTemplatesWithBLOBs record);

    List<WorkflowParamTemplatesWithBLOBs> selectByExampleWithBLOBs(WorkflowParamTemplatesExample example);

    List<WorkflowParamTemplates> selectByExample(WorkflowParamTemplatesExample example);

    WorkflowParamTemplatesWithBLOBs selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") WorkflowParamTemplatesWithBLOBs record, @Param("example") WorkflowParamTemplatesExample example);

    int updateByExampleWithBLOBs(@Param("record") WorkflowParamTemplatesWithBLOBs record, @Param("example") WorkflowParamTemplatesExample example);

    int updateByExample(@Param("record") WorkflowParamTemplates record, @Param("example") WorkflowParamTemplatesExample example);

    int updateByPrimaryKeySelective(WorkflowParamTemplatesWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(WorkflowParamTemplatesWithBLOBs record);

    int updateByPrimaryKey(WorkflowParamTemplates record);
}