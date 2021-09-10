package io.rackshift.engine.taskgraph;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.engine.taskobject.BaseTaskObject;
import lombok.Data;

import java.io.InputStream;

@Data
public class BaseTaskGraph {
    protected String friendlyName;
    protected String injectableName;
    protected JSONObject options;
    protected JSONArray tasks;

    public static void main(String[] args) {
        InputStream in = BaseTaskObject.class.getResourceAsStream("io/rackshift/engine/taskgraph/install-centos-graph.js");

    }
}
