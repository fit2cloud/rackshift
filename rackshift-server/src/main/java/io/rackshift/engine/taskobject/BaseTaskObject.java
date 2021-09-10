package io.rackshift.engine.taskobject;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor()
public class BaseTaskObject {
    protected String friendlyName;
    protected String injectableName;
    protected String optionsSchema;
    protected JSONObject options;
    protected JSONObject properties;
    protected String implementsTask;
}
