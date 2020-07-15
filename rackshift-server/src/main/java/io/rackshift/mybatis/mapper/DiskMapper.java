package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.Disk;
import io.rackshift.mybatis.domain.DiskExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DiskMapper {
    long countByExample(DiskExample example);

    int deleteByExample(DiskExample example);

    int deleteByPrimaryKey(String id);

    int insert(Disk record);

    int insertSelective(Disk record);

    List<Disk> selectByExample(DiskExample example);

    Disk selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Disk record, @Param("example") DiskExample example);

    int updateByExample(@Param("record") Disk record, @Param("example") DiskExample example);

    int updateByPrimaryKeySelective(Disk record);

    int updateByPrimaryKey(Disk record);
}