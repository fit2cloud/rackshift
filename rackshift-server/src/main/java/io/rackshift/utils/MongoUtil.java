package io.rackshift.utils;

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
}
