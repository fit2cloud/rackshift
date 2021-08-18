package io.rackshift.dhcpproxy.model;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import io.rackshift.dhcpproxy.util.MongoUtil;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@lombok.Data
public class Nodes implements Serializable {
    private static Gson gson = new Gson();
    /**
     * _id : {"$oid":"611a1e0bf15c6b477953f724"}
     * name : 52:54:be:97:9b:df
     * identifiers : ["52:54:be:97:9b:df"]
     * type : compute
     * autoDiscover : false
     * relations : []
     * tags : []
     * createdAt : {"$date":1629101579969}
     * updatedAt : {"$date":1629101579969}
     */

    private IdBean _id;
    private String name;
    private String type;
    private boolean autoDiscover;
    private CreatedAtBean createdAt;
    private UpdatedAtBean updatedAt;
    private List<String> identifiers;
    private List<?> relations;
    private List<?> tags;

    @lombok.Data
    public static class IdBean implements Serializable {
        /**
         * $oid : 611a1e0bf15c6b477953f724
         */

        private String $oid;
    }

    @lombok.Data
    public static class CreatedAtBean implements Serializable {
        /**
         * $date : 1629101579969
         */

        private long $date;
    }

    @lombok.Data
    public static class UpdatedAtBean implements Serializable {
        /**
         * $date : 1629101579969
         */

        private long $date;
    }

    public boolean discovered() {
        BasicDBObject queryVO = new BasicDBObject();
        ObjectId objectId = new ObjectId(this._id.$oid);
        queryVO.put("_id", objectId);
        return MongoUtil.exist("catalogs", queryVO);
    }

    public static Nodes findByMac(String mac) {
        BasicDBObject queryVO = new BasicDBObject();
        queryVO.put("name", mac);
        FindIterable<Document> r = MongoUtil.find("nodes", new BasicDBObject());
        List<Nodes> nodeList = new LinkedList<>();
        for (Document d : r) {
            nodeList.add(gson.fromJson(d.toJson(), Nodes.class));
        }
        return nodeList.size() > 0 ? nodeList.get(0) : null;
    }

    public boolean isRunningTask() {
        BasicDBObject q = new BasicDBObject();
        q.put("node", this._id.$oid);
        q.put("_status", "running");
        return MongoUtil.exist("graphobjects", q);
    }
}
