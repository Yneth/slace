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

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Configuration
@Order(HIGHEST_PRECEDENCE + 50)
@RequiredArgsConstructor
public class WebSocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.anyMessage().permitAll();
//        messages
//                .nullDestMatcher().authenticated()
//                .simpDestMatchers("/queue/**").authenticated()
//                .simpTypeMatchers(SimpgetMessageType().MESSAGE, SimpgetMessageType().SUBSCRIBE).denyAll()
//                .anyMessage().denyAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        // TODO: fix
        return true;
    }

}
