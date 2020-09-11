package io.rackshift.service;

import io.rackshift.model.ImageDTO;
import io.rackshift.mybatis.domain.EndpointExample;
import io.rackshift.mybatis.domain.Image;
import io.rackshift.mybatis.domain.ImageExample;
import io.rackshift.mybatis.mapper.EndpointMapper;
import io.rackshift.mybatis.mapper.ImageMapper;
import io.rackshift.utils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@Service
public class ImageService {
    @Resource
    private ImageMapper imageMapper;
    @Resource
    private EndpointMapper endpointMapper;

    public Object add(ImageDTO queryVO) {
        Image image = new Image();
        BeanUtils.copyBean(image, queryVO);
        imageMapper.insertSelective(image);
        return true;
    }

    public Object update(ImageDTO queryVO) {
        Image image = new Image();
        BeanUtils.copyBean(image, queryVO);
        imageMapper.updateByPrimaryKeySelective(image);
        return true;
    }

    public Object del(String id) {
        imageMapper.deleteByPrimaryKey(id);
        return true;
    }

    public Object del(String[] ids) {
        for (String id : ids) {
            del(id);
        }
        return null;
    }

    public List<Image> list(ImageDTO queryVO) {
        ImageExample example = buildExample(queryVO);
        return imageMapper.selectByExample(example);
    }

    private ImageExample buildExample(ImageDTO queryVO) {
        return new ImageExample();
    }

    @Value("${file.upload.dir}")
    private String fileUploadBase;

    public String mount(String path, String originalName, String endpointId) {
        try {
            if (System.getProperty("os.name").toLowerCase().indexOf("linux") != -1) {
                String mountPath = originalName.substring(0, originalName.indexOf(".")) + Math.random() * 1000;
                String mountFullPath = fileUploadBase + File.separator + mountPath;
                if (!new File(mountFullPath).exists()) {
                    new File(mountFullPath).mkdirs();
                    Runtime.getRuntime().exec(String.format("mount %s %s", originalName, path));
                }
                return "http://" + getEndpointUrl(endpointId) + "common/" + mountPath;
            }
            return path;
        } catch (Exception e) {
            return path;
        }
    }

    private String getEndpointUrl(String endpointId) {
        if (StringUtils.isBlank(endpointId)) {
            return endpointMapper.selectByExample(new EndpointExample()).get(0).getIp();
        }
        return endpointMapper.selectByPrimaryKey(endpointId).getIp();
    }
}
