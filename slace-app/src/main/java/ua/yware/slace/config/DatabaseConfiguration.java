package ua.yware.slace.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories("ua.yware.slace.dao")
@EnableElasticsearchRepositories("ua.yware.slace.search")
@Configuration
public class DatabaseConfiguration {

}
