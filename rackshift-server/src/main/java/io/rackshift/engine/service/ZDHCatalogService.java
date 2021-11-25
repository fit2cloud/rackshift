package io.rackshift.engine.service;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.Catalog;
import io.rackshift.mybatis.mapper.CatalogMapper;
import io.rackshift.service.BareMetalService;
import io.rackshift.service.ProfileService;
import io.rackshift.service.TaskService;
import io.rackshift.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ZDHCatalogService {
    @Resource
    private CatalogMapper catalogMapper;

    public void saveCatalog(String bareMetalId, JSONObject result) {
        Catalog catalog = new Catalog();
        catalog.setId(UUIDUtil.newUUID());
        catalog.setBareMetalId(bareMetalId);
        catalog.setSource(result.getString("source").trim());
        catalog.setData(result.getString("data"));
        catalog.setCreateTime(System.currentTimeMillis());
        catalogMapper.insert(catalog);
    }
}
