package io.rackshift.service;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.engine.util.EjsService;
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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    @Resource
    private ProfileMapper profileMapper;
    @Resource
    private EndpointService endpointService;
    @Resource
    private ExtImageMapper extImageMapper;
    @Resource
    private Map<String, String> renderOptions;
    @Resource
    private EjsService ejsService;

    private boolean test = false;
    private String content;

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
        profileMapper.updateByPrimaryKeySelective(queryVO);
        return true;
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

    public Profile getProfileByName(String name) {
        ProfileExample e = new ProfileExample();
        e.createCriteria().andNameEqualTo(name);
        List<Profile> profiles = profileMapper.selectByExampleWithBLOBs(e);
        if (profiles.size() > 0) {
            return profiles.get(0);
        }
        return null;
    }

    public String getProfileContentByName(Map profileOptionMap) {
        if (profileOptionMap == null || profileOptionMap.get("profile") == null)
            return "echo RackShift: No active task is running !";
        ProfileExample e = new ProfileExample();
        e.createCriteria().andNameEqualTo((String) profileOptionMap.get("profile"));
        List<Profile> profiles = profileMapper.selectByExampleWithBLOBs(e);
        JSONObject options = JSONObject.parseObject((String) profileOptionMap.get("options"));
        options.put("macaddress", profileOptionMap.get("macaddress"));
        if (profiles.size() > 0) {
            LogUtil.info("profile:" + profileOptionMap.get("profile"));
            if (profiles.get(0).getName().endsWith(".ipxe"))
                return ejsService.renderWithEjs(getProfileByName("boilerplate.ipxe").getContent() + profiles.get(0).getContent(), options);
            else
                return ejsService.renderWithEjs(profiles.get(0).getContent(), options);
        }
        return "echo RackShift: No profile is provided !";

    }

    public String render(String originContent, Map optionsForRender) {
        Pattern p = Pattern.compile("<%=(\\w+)%>");
        Matcher m = p.matcher(originContent);
        while (m.find()) {
            originContent = originContent.replace(m.group(), ((String) optionsForRender.get(m.group(1))));
        }
        if (!test) {
            return new String(originContent.getBytes(StandardCharsets.UTF_8), StandardCharsets.US_ASCII);
        } else {
            return new String(content.getBytes(StandardCharsets.UTF_8), StandardCharsets.US_ASCII);
        }
    }

    public String getDefaultProfile(String profileName) {
        Profile profile = getProfileByName(profileName);
        if (profile != null) {
            Map<String, String> options = new HashMap<>();
            options.putAll(renderOptions);
            options.put("macaddress", "");
            return ejsService.renderWithEjs(getProfileByName("boilerplate.ipxe").getContent() + profile.getContent(), (JSONObject) JSONObject.toJSON(options));
        }
        return ejsService.renderWithEjs(profile.getContent(), (JSONObject) JSONObject.toJSON(renderOptions));
    }

    public void test(String content, boolean test) {
        if (test) {
            this.test = true;
            this.content = content;
        } else {
            this.test = false;
        }
    }
}
