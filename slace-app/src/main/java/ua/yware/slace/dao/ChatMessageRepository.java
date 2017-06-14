package ua.yware.slace.dao;

import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;
import java.util.List;

import ua.yware.slace.model.ChatMessage;
import ua.yware.slace.model.User;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, BigInteger> {

    List<ChatMessage> findAllBySenderAndReceiver(User sender, User receiver);

}
