package io.rackshift.engine.service;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.MqConstants;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.Template;
import io.rackshift.service.BareMetalService;
import io.rackshift.service.ProfileService;
import io.rackshift.service.TaskService;
import io.rackshift.service.TemplateService;
import io.rackshift.utils.MqUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ZDHTemplatesService {
    @Resource
    private ProfileService profileService;
    @Resource
    private TaskService taskService;
    @Resource
    private BareMetalService bareMetalService;
    @Resource
    private TemplateService templateService;

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
                return render(template.getContent(), param);
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
}
