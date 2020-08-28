package io.rackshift.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoDriverInformation;
import com.mongodb.client.MongoClient;
import com.mongodb.client.internal.MongoClientImpl;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.mybatis.domain.SystemParameter;
import io.rackshift.mybatis.mapper.SystemParameterMapper;
import io.rackshift.utils.MongoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class MongoConfig {
    @Value("${run.mode:local}")
    private String runMode;
    @Value("${mongo.url:localhost}")
    private String mongoUrl;
    @Resource
    private SystemParameterMapper systemParameterMapper;

    @Bean
    public MongoClient mongoClient() {
        if ("local".equalsIgnoreCase(runMode)) {
            MongoClient client = new MongoClientImpl(MongoClientSettings.builder().build(), MongoDriverInformation.builder().build());
            MongoUtil.setMongoClient(client);
            return client;
        } else {
            SystemParameter endPoint = systemParameterMapper.selectByPrimaryKey(ServiceConstants.ENDPOINT_KEY);
            MongoClientSettings clientSettings = MongoClientSettings.builder().applyConnectionString(new ConnectionString("mongodb://" + (endPoint.getParamValue().contains(":") ? endPoint.getParamValue().substring(0, endPoint.getParamValue().indexOf(":")) + ":27017" : endPoint.getParamValue()))).build();
            MongoClient client = new MongoClientImpl(clientSettings, MongoDriverInformation.builder().build());
            MongoUtil.setMongoClient(client);
            return client;
        }
    }
}
