package io.rackshift.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.mybatis.domain.BareMetalRule;
import io.rackshift.mybatis.domain.BareMetalRuleExample;
import io.rackshift.mybatis.mapper.BareMetalRuleMapper;
import io.rackshift.utils.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SyncRuleJob {
    @Resource
    private BareMetalRuleMapper bareMetalRuleMapper;

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void run() {
        BareMetalRuleExample e = new BareMetalRuleExample();
        List<BareMetalRule> rules = bareMetalRuleMapper.selectByExample(e);

        rules.forEach(r -> {
            String credentials = r.getCredentialParam();
            if(StringUtils.isNotBlank(credentials)){
                JSONArray paramsArr = JSONArray.parseArray(credentials);
                String startIp , endIp, netMask;
                startIp = r.getStartIp();
                endIp = r.getEndIp();
                netMask = r.getMask();


                List<String> ips = IpUtil.getIpRange(startIp, endIp, netMask);
                for (String ip : ips) {

                }

                paramsArr.forEach(p->{
                    JSONObject param = (JSONObject)p;

                });
            }
        });
    }

}
