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

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.yware.slace.config.jwt.exception.InvalidTokenException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final TokenService tokenService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        if (!(servletRequest instanceof HttpServletRequest)) {
            throw new IllegalStateException("JwtAuthenticationFilter supports only HTTP.");
        }

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        String authentication = httpRequest.getHeader("Authorization");
        if (StringUtils.hasText(authentication) && authentication.startsWith("Bearer ")) {
            String token = authentication.substring(7);

            try {
                tokenService.validateToken(token);
                SecurityContextHolder.getContext().setAuthentication(tokenService.getAuthentication(token));
            } catch (InvalidTokenException ignore) {
                log.trace("Token failed to pass validation.", ignore);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
