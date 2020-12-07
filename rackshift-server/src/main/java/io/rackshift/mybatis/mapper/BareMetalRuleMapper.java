package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.BareMetalRule;
import io.rackshift.mybatis.domain.BareMetalRuleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BareMetalRuleMapper {
    long countByExample(BareMetalRuleExample example);

    int deleteByExample(BareMetalRuleExample example);

    int deleteByPrimaryKey(String id);

    int insert(BareMetalRule record);

    int insertSelective(BareMetalRule record);

    List<BareMetalRule> selectByExampleWithBLOBs(BareMetalRuleExample example);

    List<BareMetalRule> selectByExample(BareMetalRuleExample example);

    BareMetalRule selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") BareMetalRule record, @Param("example") BareMetalRuleExample example);

    int updateByExampleWithBLOBs(@Param("record") BareMetalRule record, @Param("example") BareMetalRuleExample example);

    int updateByExample(@Param("record") BareMetalRule record, @Param("example") BareMetalRuleExample example);

    int updateByPrimaryKeySelective(BareMetalRule record);

    int updateByPrimaryKeyWithBLOBs(BareMetalRule record);

    int updateByPrimaryKey(BareMetalRule record);
}