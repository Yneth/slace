package ua.yware.slace.dao;

import java.math.BigInteger;
import java.util.List;

import ua.yware.slace.model.ChatMessage;

import org.springframework.data.repository.CrudRepository;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, BigInteger> {

    List<ChatMessage> findAllByFromIdAndToId(BigInteger from, BigInteger to);

}
