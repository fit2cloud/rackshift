package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.InstallScript;
import io.rackshift.mybatis.domain.InstallScriptExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface InstallScriptMapper {
    long countByExample(InstallScriptExample example);

    int deleteByExample(InstallScriptExample example);

    int deleteByPrimaryKey(String id);

    int insert(InstallScript record);

    int insertSelective(InstallScript record);

    List<InstallScript> selectByExampleWithBLOBs(InstallScriptExample example);

    List<InstallScript> selectByExample(InstallScriptExample example);

    InstallScript selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") InstallScript record, @Param("example") InstallScriptExample example);

    int updateByExampleWithBLOBs(@Param("record") InstallScript record, @Param("example") InstallScriptExample example);

    int updateByExample(@Param("record") InstallScript record, @Param("example") InstallScriptExample example);

    int updateByPrimaryKeySelective(InstallScript record);

    int updateByPrimaryKeyWithBLOBs(InstallScript record);

    int updateByPrimaryKey(InstallScript record);
}