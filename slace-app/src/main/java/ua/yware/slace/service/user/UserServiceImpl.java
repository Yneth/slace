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

package ua.yware.slace.service.user;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import ua.yware.slace.dao.UserRepository;
import ua.yware.slace.model.User;
import ua.yware.slace.web.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getById(BigInteger id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such user!"));
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

}
