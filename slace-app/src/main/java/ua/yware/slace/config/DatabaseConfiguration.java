package ua.yware.slace.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories("ua.yware.slace.dao")
public class DatabaseConfiguration {

}
