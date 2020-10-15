package io.rackshift.job;

import io.rackshift.constants.ServiceConstants;
import io.rackshift.metal.sdk.util.HttpFutureUtils;
import io.rackshift.mybatis.domain.Endpoint;
import io.rackshift.service.EndpointService;
import io.rackshift.utils.ProxyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EndpointPoller {
    @Resource
    private EndpointService endpointService;

    @Scheduled(fixedDelay = 60000)
    public void run() {
        List<Endpoint> endpointList = endpointService.getAllEndPoints();

        endpointList.forEach(e -> {
            try {
                String res = HttpFutureUtils.getHttp(String.format("http://%s:8083/endpoint/heartBeat", e.getIp()), ProxyUtil.getHeaders());
                if (StringUtils.isNotBlank(res) && "ok".equalsIgnoreCase(res)) {
                    e.setStatus(ServiceConstants.EndpointStatusEnum.Online.name());
                } else {
                    e.setStatus(ServiceConstants.EndpointStatusEnum.Offline.name());
                }
            } catch (Exception e1) {
                e.setStatus(ServiceConstants.EndpointStatusEnum.Offline.name());
            }
            endpointService.update(e);
        });

    }
}
