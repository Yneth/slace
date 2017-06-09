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

package ua.yware.slace.web.websocket;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import ua.yware.slace.dao.PremiseReservationRepository;
import ua.yware.slace.facade.ChatFacade;
import ua.yware.slace.model.ChatMessage;
import ua.yware.slace.model.User;
import ua.yware.slace.service.dto.ChatMessageDto;
import ua.yware.slace.service.user.CurrentUserService;
import ua.yware.slace.web.rest.form.ChatMessageForm;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("${api.prefix}")
@RequiredArgsConstructor
public class ChatController {

    private final PremiseReservationRepository premiseReservationRepository;
    private final ChatFacade chatFacade;
    private final CurrentUserService currentUserService;
    private final SimpMessageSendingOperations messagingTemplate;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/chat/{userId}")
    public List<ChatMessage> getChatHistory(@PathVariable("userId") Long userId) {
        return chatFacade.loadHistory(userId);
    }

    @MessageMapping("/chat.private")
    public void sendPrivateMessage(ChatMessageForm chatMessageForm) {
        User currentUser = currentUserService.getCurrentUser();
        ChatMessageDto chatMessageDto = new ChatMessageDto();

        chatMessageDto.setSenderId(currentUser.getId().toString());
        chatMessageDto.setCreationDate(LocalDateTime.now());
        chatMessageDto.setMessage(chatMessageForm.getMessage());

        if (premiseReservationRepository.findFirstByUserIdAndPremiseOwnerId(
                currentUser.getId(), new BigInteger(chatMessageForm.getReceiverId())) == null) {
            messagingTemplate.convertAndSendToUser(currentUser.getId().toString(),
                    "/queue/private", "cannot write this user");
            return;
        }

        messagingTemplate.convertAndSendToUser(chatMessageForm.getReceiverId(),
                "/queue/private", chatMessageDto);
    }

}
