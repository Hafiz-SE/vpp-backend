package org.opensource.energy.vpp_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableJpaAuditing
@EnableRetry
public class VppBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VppBackendApplication.class, args);
    }
}
