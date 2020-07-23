package io.rackshift.service;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.BasicDBObject;
import io.rackshift.utils.MongoUtil;
import io.rackshift.utils.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class WorkflowService {
    @Resource
    private RackHDService rackHDService;

    public Pager<JSONArray> getGraphDefinitions(String name, int page, int pageSize) {
        String collections = "graphdefinitions";
        Pattern pattern = Pattern.compile(".*" + name + ".*", Pattern.CASE_INSENSITIVE);
        List<BasicDBObject> cond = new ArrayList<BasicDBObject>() {{
            add(new BasicDBObject("friendlyName", pattern));
            add(new BasicDBObject("injectableName", pattern));
        }};
        if (StringUtils.isNotBlank(name)) {
            return MongoUtil.page(collections, new BasicDBObject("$or", cond), page, pageSize);
        }
        return MongoUtil.page(collections, new BasicDBObject(), page, pageSize);
    }
}
