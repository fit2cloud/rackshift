package io.rackshift.mybatis.mapper;

import io.rackshift.mybatis.domain.Catalog;
import io.rackshift.mybatis.domain.CatalogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CatalogMapper {
    long countByExample(CatalogExample example);

    int deleteByExample(CatalogExample example);

    int deleteByPrimaryKey(String id);

    int insert(Catalog record);

    int insertSelective(Catalog record);

    List<Catalog> selectByExampleWithBLOBs(CatalogExample example);

    List<Catalog> selectByExample(CatalogExample example);

    Catalog selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Catalog record, @Param("example") CatalogExample example);

    int updateByExampleWithBLOBs(@Param("record") Catalog record, @Param("example") CatalogExample example);

    int updateByExample(@Param("record") Catalog record, @Param("example") CatalogExample example);

    int updateByPrimaryKeySelective(Catalog record);

    int updateByPrimaryKeyWithBLOBs(Catalog record);

    int updateByPrimaryKey(Catalog record);
}