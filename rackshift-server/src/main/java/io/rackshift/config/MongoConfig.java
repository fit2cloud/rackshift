package io.rackshift.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoDriverInformation;
import com.mongodb.client.MongoClient;
import com.mongodb.client.internal.MongoClientImpl;
import com.mongodb.connection.ServerSettings;
import io.rackshift.utils.MongoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {
    @Value("${run.mode:local}")
    private String runMode;
    @Value("${mongo.url:localhost}")
    private String mongoUrl;

    @Bean
    public MongoClient mongoClient() {
        if ("local".equalsIgnoreCase(runMode)) {
            MongoClient client = new MongoClientImpl(MongoClientSettings.builder().build(), MongoDriverInformation.builder().build());
            MongoUtil.setMongoClient(client);
            return client;
        } else {
            //:todo
            ServerSettings serverSettings = ServerSettings.builder().applyConnectionString(new ConnectionString(mongoUrl)).build();
//            MongoClientSettings clientSettings = MongoClientSettings.builder().applyToServerSettings(()=>serverSettings);
            return new MongoClientImpl(MongoClientSettings.builder().build(), MongoDriverInformation.builder().build());
        }
    }
}
