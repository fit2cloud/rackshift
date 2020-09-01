package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.Endpoint;
import io.rackshift.mybatis.domain.EndpointExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EndpointMapper {
    long countByExample(EndpointExample example);

    int deleteByExample(EndpointExample example);

    int deleteByPrimaryKey(String id);

    int insert(Endpoint record);

    int insertSelective(Endpoint record);

    List<Endpoint> selectByExample(EndpointExample example);

    Endpoint selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Endpoint record, @Param("example") EndpointExample example);

    int updateByExample(@Param("record") Endpoint record, @Param("example") EndpointExample example);

    int updateByPrimaryKeySelective(Endpoint record);

    int updateByPrimaryKey(Endpoint record);
}