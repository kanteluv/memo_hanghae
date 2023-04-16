package com.sparta.hanghaemamo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HanghaemamoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HanghaemamoApplication.class, args);
    }

}
