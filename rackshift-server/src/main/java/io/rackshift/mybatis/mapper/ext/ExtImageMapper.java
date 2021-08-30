package io.rackshift.mybatis.mapper.ext;

import io.rackshift.model.ImageDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtImageMapper {
    List<ImageDTO> allImage();

    int deleteTemplateById(@Param("templateId") String id);

    int deleteProfileById(@Param("profileId") String id);
}
