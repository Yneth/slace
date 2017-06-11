package ua.yware.slace.dao;

import java.math.BigInteger;

import ua.yware.slace.model.Comment;

import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, BigInteger> {

}
