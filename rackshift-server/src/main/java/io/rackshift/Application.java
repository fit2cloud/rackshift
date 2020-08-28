package io.rackshift;

import io.rackshift.constants.ServiceConstants;
import io.rackshift.mybatis.domain.SystemParameter;
import io.rackshift.mybatis.mapper.SystemParameterMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;

@ServletComponentScan
@PropertySource(value = {"file:/opt/rackshift/conf/rackshift.properties"}, encoding = "UTF-8", ignoreResourceNotFound = true)
@SpringBootApplication
@EnableScheduling
public class Application {
    @Resource
    SystemParameterMapper systemParameterMapper;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public String rackhdUrl() {
        SystemParameter systemParameter = systemParameterMapper.selectByPrimaryKey(ServiceConstants.ENDPOINT_KEY);
        if (systemParameter != null && !systemParameter.getParamValue().contains(":"))
            return "http://" + systemParameter.getParamValue() + ":9090";
        return "http://" + systemParameter.getParamValue();
    }
}
