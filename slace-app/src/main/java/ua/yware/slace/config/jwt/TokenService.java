package ua.yware.slace.config.jwt;

import org.springframework.security.core.Authentication;

public interface TokenService {

    String createToken(Authentication authentication, boolean rememberMe);

    void validateToken(String token) throws InvalidTokenException;

    Authentication getAuthentication(String token);

}
