package ua.yware.slace;

import ua.yware.slace.service.storage.StorageProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class SlaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlaceApplication.class, args);
    }

}
