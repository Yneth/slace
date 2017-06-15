package ua.yware.slace.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("security")
public class SecurityProperties {

    private Authentication authentication = Authentication.token;

    public enum Authentication {
        cookie, token;
    }

}
