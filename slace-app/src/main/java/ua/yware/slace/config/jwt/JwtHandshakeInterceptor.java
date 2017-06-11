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

import java.security.Principal;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.RequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@RequiredArgsConstructor
public class JwtHandshakeInterceptor extends DefaultHandshakeHandler {

    private final TokenService tokenService;

    public JwtHandshakeInterceptor(RequestUpgradeStrategy requestUpgradeStrategy,
                                   TokenService tokenService) {
        super(requestUpgradeStrategy);
        this.tokenService = tokenService;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest req, WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        if (!(req instanceof ServletServerHttpRequest)) {
            return null;
        }
        ServletServerHttpRequest request = (ServletServerHttpRequest) req;
        String jwt = request.getServletRequest().getParameter("jwt");
        if (StringUtils.isEmpty(jwt)) {
            return null;
        }
        Authentication authentication = tokenService.getAuthentication(jwt);
        return (Principal) authentication.getPrincipal();
    }

}
