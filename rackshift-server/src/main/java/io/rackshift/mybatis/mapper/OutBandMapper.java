package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.mybatis.domain.OutBandExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OutBandMapper {
    long countByExample(OutBandExample example);

    int deleteByExample(OutBandExample example);

    int deleteByPrimaryKey(String id);

    int insert(OutBand record);

    int insertSelective(OutBand record);

    List<OutBand> selectByExample(OutBandExample example);

    OutBand selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") OutBand record, @Param("example") OutBandExample example);

    int updateByExample(@Param("record") OutBand record, @Param("example") OutBandExample example);

    int updateByPrimaryKeySelective(OutBand record);

    int updateByPrimaryKey(OutBand record);
}