package com.eservice.sinsimiot.config;

import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
//发现高版本的spring boot已经移除了这个类
//        需要改成以下这个
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class MultiDBConfig {
    @Bean(name = "mysqlDb")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource mysqlDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mysqlJdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("mysqlDb") DataSource dsMySQL) {
        return new JdbcTemplate(dsMySQL);
    }


    /**
     * 配置Mongodb的信息
     */
//    @PropertySource(value = { "mongodb.properties" })
    @Configuration
    public class MongoDBConfig {

        @Value("${spring.data.mongodb.url}")
        private String mongoUrl;

        @Value("${spring.data.mongodb.database}")
        private String mongoDateBase;

        public @Bean MongoTemplate mongoTemplate() {
            return new MongoTemplate(new SimpleMongoClientDbFactory(MongoClients.create(mongoUrl), mongoDateBase));
        }

    }
}
