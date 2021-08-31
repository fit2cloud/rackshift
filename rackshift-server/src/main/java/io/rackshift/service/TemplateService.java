package io.rackshift.service;

import io.rackshift.constants.ServiceConstants;
import io.rackshift.model.RSException;
import io.rackshift.model.TemplateDTO;
import io.rackshift.mybatis.domain.Template;
import io.rackshift.mybatis.domain.TemplateExample;
import io.rackshift.mybatis.mapper.TemplateMapper;
import io.rackshift.mybatis.mapper.ext.ExtImageMapper;
import io.rackshift.utils.BeanUtils;
import io.rackshift.utils.LogUtil;
import io.rackshift.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
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
public class TemplateService {
    @Resource
    private TemplateMapper templateMapper;
    @Resource
    private EndpointService endpointService;
    @Resource
    private ExtImageMapper extImageMapper;

    public Object add(TemplateDTO queryVO) throws Exception {
        if (StringUtils.isNotBlank(queryVO.getName())) {
            TemplateExample e = new TemplateExample();
            e.createCriteria().andNameEqualTo(queryVO.getName());
            if (templateMapper.selectByExample(e).size() > 0) {
                RSException.throwExceptions("Template already exists!");
            }
        }
        Template image = new Template();
        BeanUtils.copyBean(image, queryVO);
        if (uploadToServer(queryVO)) {
            image.setId(UUIDUtil.newUUID());
            templateMapper.insertSelective(image);
            return true;
        }
        return false;
    }

    public Object update(Template queryVO) throws Exception {
        Template template = templateMapper.selectByPrimaryKey(queryVO.getId());
//        if (ServiceConstants.SYSTEM.equalsIgnoreCase(template.getType())) {
//            return false;
//        }
        queryVO.setId(template.getId());
        if (uploadToServer(queryVO)) {
            templateMapper.updateByPrimaryKeySelective(queryVO);
            return true;
        }
        return false;
    }

    public boolean del(String id) {
        Template template = templateMapper.selectByPrimaryKey(id);
        if (template == null) {
            return false;
        }
        if (template.getType().equalsIgnoreCase(ServiceConstants.SYSTEM)) {
            return false;
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String url = "http://" + endpointService.getAllEndPoints().stream().filter(s -> s.getType().equalsIgnoreCase(ServiceConstants.EndPointType.main_endpoint.name())).collect(Collectors.toList()).get(0).getIp() + ":9090/api/2.0/templates/library/" + template.getName();
        HttpDelete httpDelete = new HttpDelete(url);// 创建httpPut

        try {
            CloseableHttpResponse response = httpclient.execute(httpDelete);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
                LogUtil.error("删除template失败！可能是文件不存在！" + template.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.error("删除template失败！异常" + e);
        }
        templateMapper.deleteByPrimaryKey(id);
        extImageMapper.deleteTemplateById(template.getId());
        return true;
    }

    public Object del(String[] ids) {
        for (String id : ids) {
            if (ServiceConstants.SYSTEM.equalsIgnoreCase(templateMapper.selectByPrimaryKey(id).getType())) {
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

    public List<Template> list(TemplateDTO queryVO) {
        TemplateExample example = buildExample(queryVO);
        return templateMapper.selectByExampleWithBLOBs(example);
    }

    private TemplateExample buildExample(TemplateDTO queryVO) {
        return new TemplateExample();
    }

    public Template getById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return templateMapper.selectByPrimaryKey(id);
    }

    private boolean uploadToServer(Template template) throws Exception {

        File temp = File.createTempFile(template.getName(), null);
        FileOutputStream fileOutputStream = new FileOutputStream(temp);
        fileOutputStream.write(template.getContent().getBytes());
        fileOutputStream.close();

        CloseableHttpClient httpclient = HttpClients.createDefault();
        String url = "http://" + endpointService.getAllEndPoints().stream().filter(s -> s.getType().equalsIgnoreCase(ServiceConstants.EndPointType.main_endpoint.name())).collect(Collectors.toList()).get(0).getIp() + ":9090/api/2.0/templates/library/" + template.getName();
        HttpPut httpPut = new HttpPut(url);// 创建httpPut
        httpPut.setHeader("Accept", "application/x-www-form-urlencoded");
        httpPut.setHeader("Content-Type", "application/x-www-form-urlencoded");
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
            LogUtil.error("请求失败:" + e);
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

    public Object getAllTemplates() {
        return list(new TemplateDTO());
    }

}
