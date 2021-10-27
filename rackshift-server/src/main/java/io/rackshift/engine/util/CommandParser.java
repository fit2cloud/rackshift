package io.rackshift.engine.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eclipsesource.v8.*;
import com.eclipsesource.v8.utils.MemoryManager;
import com.eclipsesource.v8.utils.V8ObjectUtils;
import com.google.gson.Gson;
import io.rackshift.engine.service.ZDHCatalogService;
import io.rackshift.model.RSException;
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
    @Resource
    private ZDHCatalogService zdhCatalogService;
    private ThreadLocal<V8> v8ThreadLocal = new ThreadLocal<>();
    private Gson gson = new Gson();

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
     * @throws IOException
     */
    public void saveCatalog(String bareMetalId, JSONArray tasksObj) throws IOException {
        init();
        V8 v8 = v8ThreadLocal.get();
        MemoryManager memoryManager = new MemoryManager(v8);
        BlockingQueue<JSONArray> blockingQueue = new ArrayBlockingQueue<JSONArray>(1);

        JavaVoidCallback successCallback = (v8Object, v8Array) -> {
            Object resultObj = V8ObjectUtils.getValue(v8Array, 0);
            if (resultObj != null) {
                // use gson to fix fastjson
                blockingQueue.add(JSONArray.parseArray(gson.toJson(resultObj)));
            }
        };

        JavaVoidCallback errorCallback = (v8Object, v8Array) -> {
            Object resultObj = V8ObjectUtils.getValue(v8Array, 0);
            if (resultObj != null) {
                blockingQueue.add(JSONArray.parseArray(gson.toJson(resultObj)));
            }
        };

        v8.registerJavaMethod(successCallback, "success");
        v8.registerJavaMethod(errorCallback, "error");
        String script = "let cp = new commandParserFactory(_); cp[\"parseTasks\"](" + tasksObj.toJSONString() + ").then(success).catch(error);";
        v8.executeScript(script);
        try {
            JSONArray result = blockingQueue.poll(1, TimeUnit.MINUTES);
            if (result != null)
                result.stream().forEach(r -> {
                    if (((JSONObject) r).containsKey("store") && ((JSONObject) r).getBoolean("store")) {
                        zdhCatalogService.saveCatalog(bareMetalId, ((JSONObject) r));
                    }
                });
            else
                RSException.throwExceptions("ss");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        memoryManager.release();
        v8ThreadLocal.remove();
    }
}