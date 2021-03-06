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

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongobeeConfiguration {

    @Bean
    public Mongobee mongobee(MongoTemplate mongoTemplate, MongoProperties mongoProperties) {
        Mongobee runner = new Mongobee(mongoProperties.determineUri());

        runner.setChangeLogsScanPackage("ua.yware.slace.config.changeset");
        runner.setMongoTemplate(mongoTemplate);

        return runner;
    }

}
