package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.SwitchRule;
import io.rackshift.mybatis.domain.SwitchRuleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SwitchRuleMapper {
    long countByExample(SwitchRuleExample example);

    int deleteByExample(SwitchRuleExample example);

    int deleteByPrimaryKey(String id);

    int insert(SwitchRule record);

    int insertSelective(SwitchRule record);

    List<SwitchRule> selectByExampleWithBLOBs(SwitchRuleExample example);

    List<SwitchRule> selectByExample(SwitchRuleExample example);

    SwitchRule selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") SwitchRule record, @Param("example") SwitchRuleExample example);

    int updateByExampleWithBLOBs(@Param("record") SwitchRule record, @Param("example") SwitchRuleExample example);

    int updateByExample(@Param("record") SwitchRule record, @Param("example") SwitchRuleExample example);

    int updateByPrimaryKeySelective(SwitchRule record);

    int updateByPrimaryKeyWithBLOBs(SwitchRule record);

    int updateByPrimaryKey(SwitchRule record);
}