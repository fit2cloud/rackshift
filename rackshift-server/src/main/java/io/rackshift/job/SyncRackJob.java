package io.rackshift.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import io.rackshift.constants.RackHDConstants;
import io.rackshift.model.MachineEntity;
import io.rackshift.service.BareMetalService;
import io.rackshift.service.RackHDService;
import io.rackshift.utils.MongoUtil;
import io.rackshift.utils.RackHDHttpClientUtil;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service
public class SyncRackJob {
    @Value("rackhd.url:http://127.0.0.1:9090")
    private String rackhdUrl;
    @Resource
    private RackHDService rackHDService;
    @Resource
    private BareMetalService bareMetalService;
    @Value("${run.mode:local}")
    private String runModel;
    @Resource
    private MongoClient mongoClient;

    @Scheduled(fixedDelay = 1000)
    public void run() {
        List<MachineEntity> entities = new LinkedList<>();
        JSONArray nodesArr = null;
        if ("release".equalsIgnoreCase(runModel)) {
            nodesArr = JSONArray.parseArray(RackHDHttpClientUtil.get(rackhdUrl + RackHDConstants.NODES_URL, null));

        } else {
            nodesArr = getLocalNodes();
        }
        for (int i = 0; i < nodesArr.size(); i++) {
            JSONObject nodeObj = nodesArr.getJSONObject(i);
            String type = nodeObj.getString("type");
            if ("compute".equalsIgnoreCase(type)) {
                MachineEntity en = rackHDService.getNodeEntity(rackhdUrl, nodeObj.getString("id"), null);
                if (en != null) {
                    entities.add(en);
                }
            }
        }

        entities.forEach(e -> {
            bareMetalService.saveOrUpdateEntity(e);
        });
    }

    private JSONArray getLocalNodes() {
        JSONArray arr = new JSONArray();
        MongoUtil.setMongoClient(mongoClient);
        String nodes = "nodes";
        FindIterable<Document> nodesR = MongoUtil.find(nodes, new BasicDBObject());
        for (Document document : nodesR) {
            JSONObject node = (JSONObject) JSONObject.toJSON(document);
            node.put("id", document.get("_id").toString());
            arr.add(node);
        }
        return arr;
    }
}
