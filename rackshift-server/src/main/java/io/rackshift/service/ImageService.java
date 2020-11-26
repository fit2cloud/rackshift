package io.rackshift.service;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.metal.sdk.util.HttpFutureUtils;
import io.rackshift.model.ImageDTO;
import io.rackshift.model.RSException;
import io.rackshift.mybatis.domain.EndpointExample;
import io.rackshift.mybatis.domain.Image;
import io.rackshift.mybatis.domain.ImageExample;
import io.rackshift.mybatis.mapper.EndpointMapper;
import io.rackshift.mybatis.mapper.ImageMapper;
import io.rackshift.utils.BeanUtils;
import io.rackshift.utils.ProxyUtil;
import io.rackshift.utils.Translator;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.UrlEncoded;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImageService {
    @Resource
    private ImageMapper imageMapper;
    @Resource
    private EndpointMapper endpointMapper;

    public Object add(ImageDTO queryVO) {
        Image image = new Image();
        BeanUtils.copyBean(image, queryVO);
        Map<String, String> map = mount(queryVO);
        image.setUrl(map.get("url"));
        image.setMountPath(map.get("mountPath"));
        imageMapper.insertSelective(image);
        return true;
    }

    public Object update(Image queryVO) {
        Image image = new Image();
        BeanUtils.copyBean(image, queryVO);
        Image dbImage = imageMapper.selectByPrimaryKey(queryVO.getId());
        if (!StringUtils.isAnyBlank(queryVO.getUrl(), dbImage.getUrl()) && !queryVO.getUrl().equals(dbImage.getUrl())) {
            if (umount(queryVO)) {
                image.setFilePath(null);
                image.setMountPath(null);
            }
        }
        imageMapper.updateByPrimaryKey(image);

        return true;
    }

    public Object del(String id) {
        Image image = imageMapper.selectByPrimaryKey(id);
        if (image == null) return false;
        if (umount(image)) {
            imageMapper.deleteByPrimaryKey(id);
            return true;
        }
        return false;
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

    public Map mount(Image imageDTO) {
        String mountPath = null;
        String url = null;
        try {
            if (!imageDTO.getOriginalName().endsWith("iso")) {
                RSException.throwExceptions("i18n_file_must_be_iso");
            }
            String mountDirName = imageDTO.getOriginalName().substring(0, imageDTO.getOriginalName().indexOf(".")) + (int) (Math.random() * 10000);
            mountPath = fileUploadBase + File.separator + mountDirName;
            String res = HttpFutureUtils.getHttp(String.format("http://" + getEndpointUrl(imageDTO.getEndpointId()) + ":8083/image/mount?filePath=%s&fileUploadBase=%s&mountDirName=%s", UrlEncoded.encodeString(imageDTO.getFilePath()), UrlEncoded.encodeString(fileUploadBase), UrlEncoded.encodeString(mountDirName)), ProxyUtil.
                    getHeaders());
            if (StringUtils.isNotBlank(res)) {
                JSONObject rObj = JSONObject.parseObject(res);
                if (rObj.containsKey("success") && rObj.getBoolean("success")) {
                    url = "http://" + getEndpointUrl(imageDTO.getEndpointId()) + ":9090/common/" + mountDirName;
                } else {
                    RSException.throwExceptions(rObj.getString("msg"));
                }
            }
        } catch (Exception e) {
            if (new File(imageDTO.getFilePath()).exists())
                new File(imageDTO.getFilePath()).delete();
            RSException.throwExceptions("i18n_file_upload_fail");
        }
        Map<String, String> map = new HashMap<>();
        map.put("mountPath", mountPath);
        map.put("url", url);
        return map;
    }

    private boolean umount(Image imageDTO) {
        try {
            if (!imageDTO.getOriginalName().endsWith("iso")) {
                RSException.throwExceptions("i18n_file_must_be_iso");
            }
            if (getEndpointUrl(imageDTO.getEndpointId()) == null) {
                return true;
            }
            String res = HttpFutureUtils.getHttp(String.format("http://" + getEndpointUrl(imageDTO.getEndpointId()) + ":8083/image/umount?filePath=%s&mountPath=%s", UrlEncoded.encodeString(imageDTO.getFilePath()), UrlEncoded.encodeString(imageDTO.getMountPath())), ProxyUtil.getHeaders());
            if (StringUtils.isNotBlank(res)) {
                JSONObject rObj = JSONObject.parseObject(res);
                if (rObj.containsKey("success") && rObj.getBoolean("success")) {
                    return true;
                } else {
                    RSException.throwExceptions(rObj.getString("msg"));
                }
            }
        } catch (Exception e) {
            if (new File(imageDTO.getFilePath()).exists())
                new File(imageDTO.getFilePath()).delete();
            RSException.throwExceptions(Translator.get("i18n_file_unmount_fail"));
        }
        return false;
    }

    private String getEndpointUrl(String endpointId) {
        if (StringUtils.isBlank(endpointId)) {
            return endpointMapper.selectByExample(new EndpointExample()).get(0).getIp();
        }
        return endpointMapper.selectByPrimaryKey(endpointId).getIp();
    }
}
