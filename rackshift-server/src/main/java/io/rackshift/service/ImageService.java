package io.rackshift.service;

import io.rackshift.model.ImageDTO;
import io.rackshift.mybatis.domain.Image;
import io.rackshift.mybatis.domain.ImageExample;
import io.rackshift.mybatis.mapper.ImageMapper;
import io.rackshift.utils.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ImageService {
    @Resource
    private ImageMapper imageMapper;

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
}
