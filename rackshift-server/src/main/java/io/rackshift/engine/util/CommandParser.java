package io.rackshift.engine.util;

import com.eclipsesource.v8.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    private V8 v8;

    @PostConstruct
    public void init() throws IOException {
        NodeJS nodeJS = NodeJS.createNodeJS();
        File f1 = File.createTempFile("tempCommandParser", "js");
        File f2 = File.createTempFile("tempLodashScript", "js");

        FileOutputStream fileOutputStream = new FileOutputStream(f1);
        fileOutputStream.write(commandParserScript.getBytes(StandardCharsets.UTF_8));
        fileOutputStream.flush();
        fileOutputStream.close();

        fileOutputStream = new FileOutputStream(f2);
        fileOutputStream.write(lodashScript.getBytes(StandardCharsets.UTF_8));
        fileOutputStream.flush();
        fileOutputStream.close();

        nodeJS.require(f1);
        nodeJS.require(f2);
        v8 = nodeJS.getRuntime();
        if (f1.exists())
            f1.delete();
        if (f2.exists())
            f2.delete();
    }

    public String saveCatalog(String cmd, String catalogContent) {
        BlockingQueue<V8Value> blockingQueue = new ArrayBlockingQueue<V8Value>(2);

        JavaVoidCallback successCallback = (v8Object, v8Array) -> {
            if (v8Object != null)
                blockingQueue.add(v8Object);
            if (v8Array != null)
                blockingQueue.add(v8Array);
        };


        JavaVoidCallback errorCallback = (v8Object, v8Array) -> {
            if (v8Object != null)
                blockingQueue.add(v8Object);
            if (v8Array != null)
                blockingQueue.add(v8Array);
        };

        v8.registerJavaMethod(successCallback, "success");
        v8.registerJavaMethod(errorCallback, "error");
        String script = "let cp = new CommandParserFactory(_); cp [" + "\"" + cmd + "\"](JSON.stringify(\"" + catalogContent + "\").then(success).catch(error);";
        v8.executeScript(script);
        try {
            V8Value v8Object = blockingQueue.poll(3, TimeUnit.MINUTES);
            return v8Object.toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
