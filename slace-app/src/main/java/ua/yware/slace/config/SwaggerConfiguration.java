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

import java.nio.ByteBuffer;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;

@Configuration
@EnableSwagger2
@Profile({"swagger"})
@Slf4j
public class SwaggerConfiguration {

    @Bean
    public Docket swaggerSpringfoxDocket() {
        log.debug("Starting Swagger");

        StopWatch watch = new StopWatch();
        watch.start();
        Docket docket = (new Docket(DocumentationType.SWAGGER_2))
                .forCodeGeneration(true)
                .directModelSubstitute(ByteBuffer.class, String.class)
                .genericModelSubstitutes(new Class[]{ResponseEntity.class})
                .select()
                .paths(PathSelectors.any())
                .build();
        watch.stop();

        log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
        return docket;
    }

}
