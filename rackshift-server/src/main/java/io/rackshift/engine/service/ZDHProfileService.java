package io.rackshift.engine.service;

import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.service.BareMetalService;
import io.rackshift.service.ProfileService;
import io.rackshift.service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
     * @param macs
     * @return
     */
    public String profiles(String macs) {
        String profileName = "redirect.ipxe";
        if (StringUtils.isNotBlank(macs)) {
            BareMetal bareMetal = bareMetalService.getByPXEMAC(macs);
            if (bareMetal == null) {
                taskService.createBMAndDiscoveryGraph(macs);
            }
            if(taskService.findActiveGraph(bareMetal.getId())){
                profileName = taskService.getTaskProfile(bareMetal.getId());
            }
        }
        return profileService.getProfileContentByName(profileName);
    }
}
