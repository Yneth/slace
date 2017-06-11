package ua.yware.slace.service.user;

import java.math.BigInteger;

import ua.yware.slace.model.User;

public interface UserService {

    User getById(BigInteger id);

    User findByLogin(String login);
}
