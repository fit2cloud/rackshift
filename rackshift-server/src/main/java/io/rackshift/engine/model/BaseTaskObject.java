package io.rackshift.engine.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor()
public class BaseTaskObject implements BaseWorkflowObject {
    protected String friendlyName;
    protected String injectableName;
    protected String optionsSchema;
    protected JSONObject options;
    protected JSONObject properties;
    protected String implementsTask;
}
