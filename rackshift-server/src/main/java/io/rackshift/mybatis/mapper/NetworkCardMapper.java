package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.NetworkCard;
import io.rackshift.mybatis.domain.NetworkCardExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NetworkCardMapper {
    long countByExample(NetworkCardExample example);

    int deleteByExample(NetworkCardExample example);

    int deleteByPrimaryKey(String id);

    int insert(NetworkCard record);

    int insertSelective(NetworkCard record);

    List<NetworkCard> selectByExample(NetworkCardExample example);

    NetworkCard selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") NetworkCard record, @Param("example") NetworkCardExample example);

    int updateByExample(@Param("record") NetworkCard record, @Param("example") NetworkCardExample example);

    int updateByPrimaryKeySelective(NetworkCard record);

    int updateByPrimaryKey(NetworkCard record);
}