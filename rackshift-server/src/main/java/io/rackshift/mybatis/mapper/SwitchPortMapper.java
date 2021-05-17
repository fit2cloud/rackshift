package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.SwitchPort;
import io.rackshift.mybatis.domain.SwitchPortExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SwitchPortMapper {
    long countByExample(SwitchPortExample example);

    int deleteByExample(SwitchPortExample example);

    int deleteByPrimaryKey(String id);

    int insert(SwitchPort record);

    int insertSelective(SwitchPort record);

    List<SwitchPort> selectByExample(SwitchPortExample example);

    SwitchPort selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") SwitchPort record, @Param("example") SwitchPortExample example);

    int updateByExample(@Param("record") SwitchPort record, @Param("example") SwitchPortExample example);

    int updateByPrimaryKeySelective(SwitchPort record);

    int updateByPrimaryKey(SwitchPort record);
}