package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.Switch;
import io.rackshift.mybatis.domain.SwitchExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SwitchMapper {
    long countByExample(SwitchExample example);

    int deleteByExample(SwitchExample example);

    int deleteByPrimaryKey(String id);

    int insert(Switch record);

    int insertSelective(Switch record);

    List<Switch> selectByExample(SwitchExample example);

    Switch selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Switch record, @Param("example") SwitchExample example);

    int updateByExample(@Param("record") Switch record, @Param("example") SwitchExample example);

    int updateByPrimaryKeySelective(Switch record);

    int updateByPrimaryKey(Switch record);
}