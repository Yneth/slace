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

package ua.yware.slace.facade;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

import lombok.RequiredArgsConstructor;
import ua.yware.slace.dao.ChatMessageRepository;
import ua.yware.slace.dao.UserRepository;
import ua.yware.slace.model.ChatMessage;
import ua.yware.slace.model.User;
import ua.yware.slace.service.user.CurrentUserService;
import ua.yware.slace.service.user.UserService;

@Component
@RequiredArgsConstructor
public class ChatFacadeImpl implements ChatFacade {

    private final CurrentUserService currentUserService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public List<ChatMessage> loadHistory(BigInteger userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No such user exception."));
        User currentUser = currentUserService.getCurrentUser();
        return chatMessageRepository.findAllByFromIdAndToId(currentUser.getId(), user.getId());
    }

}
