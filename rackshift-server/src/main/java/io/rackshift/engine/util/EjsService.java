package io.rackshift.engine.util;

import com.alibaba.fastjson.JSONObject;
import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.utils.MemoryManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EjsService {
    @Resource
    private String ejsScript;
    private ThreadLocal<V8> v8ThreadLocal = new ThreadLocal<>();

    public void init() {
        if (v8ThreadLocal.get() == null) {
            NodeJS nodeJS = NodeJS.createNodeJS();
            V8 v8 = nodeJS.getRuntime();
            v8.executeVoidScript(ejsScript);
            v8ThreadLocal.set(v8);
        }
    }

    public String renderWithEjs(String originContent, JSONObject optionsForRender) {
        init();

        MemoryManager memoryManager = new MemoryManager(v8ThreadLocal.get());
        v8ThreadLocal.get().add("content", originContent);
        try {
            return v8ThreadLocal.get().executeStringScript("ejs.render(content, " +optionsForRender.toJSONString()+")");
        } catch (Exception e) {
            e.printStackTrace();
        }
        memoryManager.release();
        v8ThreadLocal.remove();
        return originContent;
    }
}
