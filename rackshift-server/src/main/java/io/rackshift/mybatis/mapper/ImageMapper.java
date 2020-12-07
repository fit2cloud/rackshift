package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.Image;
import io.rackshift.mybatis.domain.ImageExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImageMapper {
    long countByExample(ImageExample example);

    int deleteByExample(ImageExample example);

    int deleteByPrimaryKey(String id);

    int insert(Image record);

    int insertSelective(Image record);

    List<Image> selectByExampleWithBLOBs(ImageExample example);

    List<Image> selectByExample(ImageExample example);

    Image selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Image record, @Param("example") ImageExample example);

    int updateByExampleWithBLOBs(@Param("record") Image record, @Param("example") ImageExample example);

    int updateByExample(@Param("record") Image record, @Param("example") ImageExample example);

    int updateByPrimaryKeySelective(Image record);

    int updateByPrimaryKeyWithBLOBs(Image record);

    int updateByPrimaryKey(Image record);
}