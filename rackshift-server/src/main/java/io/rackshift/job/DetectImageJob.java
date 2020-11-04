package io.rackshift.job;


import io.rackshift.constants.ServiceConstants;
import io.rackshift.model.ImageDTO;
import io.rackshift.mybatis.domain.Image;
import io.rackshift.service.ImageService;
import io.rackshift.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@Service
public class DetectImageJob {
    @Resource
    ImageService imageService;
    @Value("${file.upload.dir}")
    private String fileUploadBase;

    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void run() {
        if (new File(fileUploadBase).exists()) {
            List<Image> imageList = imageService.list(new ImageDTO());
            imageList.forEach(i -> {

                i.setStatus(ServiceConstants.ImageStatusEnum.detected.name());
                if (StringUtils.isAnyBlank(i.getFilePath(), i.getMountPath())) {
                    i.setStatus(ServiceConstants.ImageStatusEnum.error.name());
                }

                if (!new File(i.getFilePath()).exists()) {
                    LogUtil.error(String.format("image file %s not exists!", i.getFilePath()));
                    i.setStatus(ServiceConstants.ImageStatusEnum.not_detected.name());
                }

                if (!new File(i.getMountPath()).exists()) {
                    LogUtil.error(String.format("image file %s not exists!", i.getMountPath()));
                    i.setStatus(ServiceConstants.ImageStatusEnum.not_detected.name());
                }
                imageService.update(i);
            });
        } else {
            LogUtil.error("fileUploadBase not exists!");
        }
    }
}
