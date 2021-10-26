package io.rackshift.engine.util;

import com.alibaba.fastjson.JSONObject;
import com.eclipsesource.v8.*;
import com.eclipsesource.v8.utils.V8ObjectUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 引用部分 rackhd 原有命令结果转换器
 */
@Component
public class CommandParser {
    @Resource
    private String commandParserScript;
    @Resource
    private String lodashScript;
    private ThreadLocal<V8> v8ThreadLocal = new ThreadLocal<>();

    public void init() {
        NodeJS nodeJS = NodeJS.createNodeJS();
        V8 v8 = nodeJS.getRuntime();
        v8.executeVoidScript(lodashScript);
        v8.executeVoidScript(commandParserScript);
        v8ThreadLocal.set(v8);
    }

    /**
     * call command-parser.js to get transformed catalogs
     *
     * @param bareMetalId
     * @param cmd
     * @param catalogObj
     * @throws IOException
     */
    public void saveCatalog(String bareMetalId, String cmd, JSONObject catalogObj) throws IOException {
        init();
        V8 v8 = v8ThreadLocal.get();
        BlockingQueue<JSONObject> blockingQueue = new ArrayBlockingQueue<JSONObject>(1);

        JavaVoidCallback successCallback = (v8Object, v8Array) -> {
            Object resultObj = V8ObjectUtils.getValue(v8Array, 0);
            if (resultObj != null) {
                blockingQueue.add((JSONObject) JSONObject.toJSON(resultObj));
            }
        };


        JavaVoidCallback errorCallback = (v8Object, v8Array) -> {
            Object resultObj = V8ObjectUtils.getValue(v8Array, 0);
            if (resultObj != null) {
                blockingQueue.add((JSONObject) JSONObject.toJSON(resultObj));
            }
        };

        v8.registerJavaMethod(successCallback, "success");
        v8.registerJavaMethod(errorCallback, "error");
        String script = "let cp = new commandParserFactory(_); cp[" + "\"" + cmd + "\"](" + catalogObj.toJSONString() + ").then(success).catch(error);";
        v8.executeScript(script);
        try {
            JSONObject result = blockingQueue.poll(1, TimeUnit.MINUTES);
            if (result.containsKey("store") && result.getBoolean("store")) {

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        v8.release();
        v8ThreadLocal.remove();
    }
}