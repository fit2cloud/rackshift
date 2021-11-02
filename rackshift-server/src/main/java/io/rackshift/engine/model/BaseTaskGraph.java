package io.rackshift.engine.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class BaseTaskGraph implements BaseWorkflowObject{
    protected String friendlyName;
    protected String injectableName;
    protected JSONObject options;
    protected JSONArray tasks;
}
