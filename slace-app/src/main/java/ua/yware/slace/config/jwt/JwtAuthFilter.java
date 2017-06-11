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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.AccessLevel;
import lombok.Setter;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

@Setter
public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {

    private String jwtParameter = "jwt";
    private String authorizationHeader = "Authorization";
    private String authorizationHeaderPrefix = "Bearer ";

    @Setter(AccessLevel.NONE)
    private final TokenService tokenService;

    public JwtAuthFilter(TokenService tokenService) {
        this(tokenService, new AntPathRequestMatcher("/login", "POST"));
    }

    public JwtAuthFilter(TokenService tokenService, String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        this.tokenService = tokenService;
    }

    public JwtAuthFilter(TokenService tokenService, RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
        this.tokenService = tokenService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse resp)
            throws AuthenticationException, IOException, ServletException {
        String token = req.getParameter(jwtParameter);
        if (StringUtils.isEmpty(token)) {
            String authorization = req.getHeader(authorizationHeader);
            if (StringUtils.hasText(token) && token.startsWith(authorizationHeaderPrefix)) {
                token = authorization.substring(authorizationHeaderPrefix.length());
            }
        }

        if (StringUtils.isEmpty(token)) {
            return null;
        }

        try {
            tokenService.validateToken(token);
        } catch (InvalidTokenException ignore) {
            throw new BadCredentialsException("Failed to parse token", ignore);
        }

        Authentication authentication = tokenService.getAuthentication(token);
        return getAuthenticationManager().authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

}
