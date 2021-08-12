package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.Profile;
import io.rackshift.mybatis.domain.ProfileExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProfileMapper {
    long countByExample(ProfileExample example);

    int deleteByExample(ProfileExample example);

    int deleteByPrimaryKey(String id);

    int insert(Profile record);

    int insertSelective(Profile record);

    List<Profile> selectByExampleWithBLOBs(ProfileExample example);

    List<Profile> selectByExample(ProfileExample example);

    Profile selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Profile record, @Param("example") ProfileExample example);

    int updateByExampleWithBLOBs(@Param("record") Profile record, @Param("example") ProfileExample example);

    int updateByExample(@Param("record") Profile record, @Param("example") ProfileExample example);

    int updateByPrimaryKeySelective(Profile record);

    int updateByPrimaryKeyWithBLOBs(Profile record);

    int updateByPrimaryKey(Profile record);
}