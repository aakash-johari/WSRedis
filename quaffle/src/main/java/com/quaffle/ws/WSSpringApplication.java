package com.quaffle.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class WSSpringApplication {
    public static void main(String[] args) {

        SpringApplication.run(WSSpringApplication.class, args);
    }
}
