package com.skilllink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.skilllink")
@EntityScan(basePackages = "com.skilllink.model")  // 👈 Add this
public class SkilllinkBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkilllinkBackendApplication.class, args);
    }

}
