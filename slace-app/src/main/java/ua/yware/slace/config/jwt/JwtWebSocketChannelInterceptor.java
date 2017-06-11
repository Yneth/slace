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

package ua.yware.slace.config.jwt;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

@Slf4j
@RequiredArgsConstructor
public class JwtWebSocketChannelInterceptor extends ChannelInterceptorAdapter {

    private final TokenService jwtTokenService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        message.getHeaders().get("nativeHeaders");
        SimpMessageHeaderAccessor wrap = SimpMessageHeaderAccessor.wrap(message);
        String authorization = wrap.getFirstNativeHeader("Authorization");
        int i = 0;
        Map<String, Object> sessionAttributes = SimpMessageHeaderAccessor.getSessionAttributes(message.getHeaders());
//        String token = (String) Optional.of(accessor.getMessageHeaders())
//                .map(m -> (GenericMessage) m.get("simpConnectMessage"))
//                .map(GenericMessage::getHeaders)
//                .map(m -> (Map) m.get("nativeHeaders"))
//                .map(m -> (List) m.get("Authorization"))
//                .map(Collection::stream)
//                .flatMap(Stream::findFirst)
//                .orElse(null);

//        String token = accessor.getFirstNativeHeader("Authorization");
//        if (StringUtils.isEmpty(token)) {
//            return message;
//        }
//        try {
//            jwtTokenService.validateToken(token);
//            accessor.setLeaveMutable(true);
//            accessor.setUser(jwtTokenService.getAuthentication(token));
//        } catch (InvalidTokenException e) {
//            log.debug("Failed to validate user token", e);
//        }
        return message;
    }

}
