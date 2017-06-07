/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.yware.slace.config;

import com.github.mongobee.Mongobee;
import com.mongodb.DB;

import org.omg.CORBA.Environment;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;

@Configuration
public class MongobeeConfiguration {

    @AutoConfigureAfter({EmbeddedMongoAutoConfiguration.class, MongoAutoConfiguration.class})
    @Configuration
    public static class MongobeeConf {

        @Bean
        public Mongobee mongobee(MongoTemplate mongoTemplate, MongoProperties mongoProperties) {
            Mongobee runner = new Mongobee(mongoProperties.determineUri());

            runner.setChangeLogsScanPackage("ua.yware.slace.config.changeset");
            runner.setMongoTemplate(mongoTemplate);

            return runner;
        }

    }

    @ConditionalOnClass(EmbeddedMongoAutoConfiguration.class)
    @Configuration
    @RequiredArgsConstructor
    public static class EmbeddedMongoPropertiesPostProcessConfiguration {

        private static final Pattern HOST_PATTERN = Pattern.compile("^mongodb://(\\w+):");
        private static final Pattern PORT_PATTERN = Pattern.compile(":(\\d+)/");
        private static final Pattern DB_NAME_PATTERN = Pattern.compile("/(\\w+)$");

        private final MongoProperties mongoProperties;

        @PostConstruct
        public void init() {
            String uri = mongoProperties.getUri();
            if (uri == null) {
                return;
            }
            Matcher hostMatcher = HOST_PATTERN.matcher(uri);
            if (hostMatcher.find()) {
                mongoProperties.setHost(hostMatcher.group(1));
            }
            Matcher portMatcher = PORT_PATTERN.matcher(uri);
            if (portMatcher.find()) {
                mongoProperties.setPort(Integer.parseInt(portMatcher.group(1)));
            }
            Matcher dbNameMatcher = DB_NAME_PATTERN.matcher(uri);
            if (dbNameMatcher.find()) {
                mongoProperties.setDatabase(dbNameMatcher.group(1));
            }
            mongoProperties.setUri(null);
        }

    }

}
