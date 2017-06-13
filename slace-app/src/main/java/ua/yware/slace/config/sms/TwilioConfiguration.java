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

package ua.yware.slace.config.sms;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.factory.MessageFactory;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TwilioProperties.class)
@RequiredArgsConstructor
public class TwilioConfiguration {

    @Bean
    public TwilioRestClient client(TwilioProperties properties) {
        TwilioProperties.Account account = properties.getAccount();
        return new TwilioRestClient(account.getSid(), account.getToken());
    }

    @Bean
    public MessageFactory messageFactory(TwilioRestClient client) {
        return client.getAccount().getMessageFactory();
    }

}
