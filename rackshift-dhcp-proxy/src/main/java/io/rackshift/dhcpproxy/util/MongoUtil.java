package io.rackshift.dhcpproxy.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import org.bson.Document;

public class MongoUtil {
    private static MongoClient mongoClient;

    public static void setMongoClient(MongoClient mongoClient) {
        MongoUtil.mongoClient = mongoClient;
    }

    public static FindIterable<Document> find(String collection, BasicDBObject queryVO) {
        return mongoClient.getDatabase("rackhd").getCollection(collection).find(queryVO);
    }

    public static boolean exist(String collection, BasicDBObject queryVO) {
        FindIterable<Document> documents = mongoClient.getDatabase("rackhd").getCollection(collection).find(queryVO);
        for (Document d : documents) {
            return true;
        }
        return false;
    }

    public static Pager<JSONArray> page(String collection, BasicDBObject queryVO, int page, int pageSize) {
        FindIterable<Document> documents = mongoClient.getDatabase("rackhd").getCollection(collection).find(queryVO);
        int total = 0;
        JSONArray arr = new JSONArray();
        for (Document document : documents) {
            JSONObject node = (JSONObject) JSONObject.toJSON(document);
            node.put("id", document.get("_id").toString());
            node.remove("_id");
            arr.add(node);
            total++;
        }
        Pager<JSONArray> pager = new Pager<>();
        pager.setItemCount(total);
        pager.setListObject(arr);
        pager.setPageCount(total / pageSize + 1);
        return pager;
    }
}
