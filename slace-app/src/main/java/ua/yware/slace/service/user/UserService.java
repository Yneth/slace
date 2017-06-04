package ua.yware.slace.service.user;

import ua.yware.slace.model.User;

public interface UserService {

    User findByLogin(String login);
}
