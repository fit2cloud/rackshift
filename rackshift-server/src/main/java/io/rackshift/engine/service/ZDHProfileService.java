package io.rackshift.engine.service;

import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.service.BareMetalService;
import io.rackshift.service.ProfileService;
import io.rackshift.service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Service
public class ZDHProfileService {
    @Resource
    private ProfileService profileService;
    @Resource
    private TaskService taskService;
    @Resource
    private BareMetalService bareMetalService;

    /**
     * 返回前端发现或者
     *
     * @param macs
     * @return
     */
    public String profiles(String macs) throws IOException, InterruptedException {
        if (StringUtils.isNotBlank(macs)) {
            BareMetal bareMetal = bareMetalService.getByPXEMAC(macs);
            if (bareMetal == null) {
                bareMetal = taskService.createBMAndDiscoveryGraph(macs);
            }
            return profileService.getProfileContentByName(taskService.getTaskProfile(bareMetal.getId()));
        }
        return profileService.getDefaultProfile("redirect.ipxe");
    }

    public void test(String content, boolean test) {
        profileService.test(content, test);
    }
}
