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

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.yware.slace.config.jwt.exception.InvalidTokenException;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
public class JwtWebSocketChannelInterceptor extends ChannelInterceptorAdapter {
//    private static final MessageMatcher<StompCommand> MATCHER =

    private final TokenService jwtTokenService;

    @Setter
    private String authorizationHeader = "Authorization";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (!StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }
        String token = accessor.getFirstNativeHeader(authorizationHeader);
        if (StringUtils.isEmpty(token)) {
            return message;
        }
        try {
            jwtTokenService.validateToken(token);
            accessor.setUser(jwtTokenService.getAuthentication(token));
        } catch (InvalidTokenException e) {
            log.debug("Failed to validate user token", e);
        }
        return message;
    }

}
