package io.rackshift.service;

import io.rackshift.constants.ServiceConstants;
import io.rackshift.model.ProfileDTO;
import io.rackshift.model.RSException;
import io.rackshift.mybatis.domain.Profile;
import io.rackshift.mybatis.domain.ProfileExample;
import io.rackshift.mybatis.mapper.ProfileMapper;
import io.rackshift.mybatis.mapper.ext.ExtImageMapper;
import io.rackshift.utils.BeanUtils;
import io.rackshift.utils.LogUtil;
import io.rackshift.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    @Resource
    private ProfileMapper profileMapper;
    @Resource
    private EndpointService endpointService;
    @Resource
    private ExtImageMapper extImageMapper;

    public Object add(ProfileDTO queryVO) throws Exception {
        if (StringUtils.isNotBlank(queryVO.getName())) {
            ProfileExample e = new ProfileExample();
            e.createCriteria().andNameEqualTo(queryVO.getName());
            if (profileMapper.selectByExample(e).size() > 0) {
                RSException.throwExceptions("Profile already exists!");
            }
        }
        Profile image = new Profile();
        BeanUtils.copyBean(image, queryVO);
        if (uploadToServer(queryVO)) {
            image.setId(UUIDUtil.newUUID());
            profileMapper.insertSelective(image);
            return true;
        }
        return false;
    }

    public Object update(Profile queryVO) throws Exception {
        Profile image = profileMapper.selectByPrimaryKey(queryVO.getId());
//        if (ServiceConstants.SYSTEM.equalsIgnoreCase(image.getType())) {
//            return false;
//        }
        queryVO.setId(image.getId());
        if (uploadToServer(queryVO)) {
            profileMapper.updateByPrimaryKeySelective(image);
            return true;
        }
        return false;
    }

    public boolean del(String id) {
        Profile profile = profileMapper.selectByPrimaryKey(id);
        if (profile == null) {
            return false;
        }
        if (profile.getType().equalsIgnoreCase(ServiceConstants.SYSTEM)) {
            return false;
        }
        profileMapper.deleteByPrimaryKey(id);
        extImageMapper.deleteProfileById(id);
        return true;
    }

    public Object del(String[] ids) {
        for (String id : ids) {
            if (ServiceConstants.SYSTEM.equalsIgnoreCase(profileMapper.selectByPrimaryKey(id).getType())) {
                return false;
            }
        }
        for (String id : ids) {
            if (!del(id)) {
                return false;
            }
        }
        return true;
    }

    public List<Profile> list(ProfileDTO queryVO) {
        ProfileExample example = buildExample(queryVO);
        return profileMapper.selectByExampleWithBLOBs(example);
    }

    private ProfileExample buildExample(ProfileDTO queryVO) {
        return new ProfileExample();
    }

    public Profile getById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return profileMapper.selectByPrimaryKey(id);
    }

    private boolean uploadToServer(Profile profile) throws Exception {
        File temp = File.createTempFile(profile.getName(), null);
        FileOutputStream fileOutputStream = new FileOutputStream(temp);
        fileOutputStream.write(profile.getContent().getBytes());
        fileOutputStream.close();

        CloseableHttpClient httpclient = HttpClients.createDefault();
        String url = "http://" + endpointService.getAllEndPoints().stream().filter(s -> s.getType().equalsIgnoreCase(ServiceConstants.EndPointType.main_endpoint.name())).collect(Collectors.toList()).get(0).getIp() + ":9090/api/2.0/profiles/library/" + profile.getName();
        HttpPut httpPut = new HttpPut(url);// 创建httpPut
        httpPut.setHeader("Accept", "application/octet-stream");
        httpPut.setHeader("Content-Type", "application/octet-stream");
        CloseableHttpResponse response = null;

        httpPut.setEntity(new FileEntity(temp));
        try {
            response = httpclient.execute(httpPut);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK || state == HttpStatus.SC_CREATED) {
                return true;
            } else {
                LogUtil.error("请求返回:" + state + "(" + url + ")");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Object getAllProfiles() {
        return list(new ProfileDTO());
    }
}
