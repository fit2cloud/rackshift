package io.rackshift.engine.service;

import com.alibaba.fastjson.JSONObject;
import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.utils.MemoryManager;
import io.rackshift.constants.MqConstants;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.Template;
import io.rackshift.service.BareMetalService;
import io.rackshift.service.TemplateService;
import io.rackshift.utils.MqUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ZDHTemplatesService {
    @Resource
    private BareMetalService bareMetalService;
    @Resource
    private TemplateService templateService;
    @Resource
    private String ejsScript;
    private ThreadLocal<V8> v8ThreadLocal = new ThreadLocal<>();

    public String getTemplateName(String templateName, String nodeId) throws IOException, InterruptedException {
        Template template = templateService.getByName(templateName);
        if (template == null) {
            return String.format("%s not exists !", templateName);
        }
        if (StringUtils.isNotBlank(nodeId)) {
            String optionStr = MqUtil.request(MqConstants.EXCHANGE_NAME, MqConstants.MQ_ROUTINGKEY_OPTIONS + nodeId, "");
            if (StringUtils.isNotBlank(optionStr)) {
                JSONObject param = JSONObject.parseObject(optionStr);
                param.put("macaddress", Optional.ofNullable(bareMetalService.getById(nodeId)).orElse(new BareMetal()).getPxeMac());
                param.put("identifier", nodeId);
                return renderWithEjs(template.getContent(), param);
            }
        }
        return template.getContent();
    }

    public String render(String originContent, JSONObject optionsForRender) {
        Pattern p = Pattern.compile("<%=\\s*(\\w+)\\s*%>");
        Matcher m = p.matcher(originContent);
        while (m.find()) {
            if (StringUtils.isNotBlank(optionsForRender.getString(m.group(1)))) {
                originContent = originContent.replace(m.group(), optionsForRender.getString(m.group(1)));
            }
        }
        return originContent;
    }

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
