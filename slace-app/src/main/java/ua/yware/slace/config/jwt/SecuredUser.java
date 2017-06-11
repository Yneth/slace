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
import java.util.Collection;

import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;

@Getter
public class SecuredUser implements Principal {

    private final String login;
    private final Collection<GrantedAuthority> authorities;

    public SecuredUser(String login, Collection<GrantedAuthority> authorities) {
        this.login = login;
        this.authorities = authorities;
    }

    @Override
    public String getName() {
        return login;
    }

}
