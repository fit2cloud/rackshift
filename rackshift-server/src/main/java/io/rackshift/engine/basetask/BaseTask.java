package io.rackshift.engine.basetask;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor()
public class BaseTask {
    protected String friendlyName;
    protected String injectableName;
    protected String runJob;
    protected String[] requiredOptions;
    protected JSONObject requiredProperties;
    protected JSONObject properties;
    //maybe json or json file name
//    protected String optionsSchema;
}
