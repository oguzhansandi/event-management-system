package com.event.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"com.event"})
@ComponentScan(basePackages = {"com.event"})
@EnableJpaRepositories(basePackages = {"com.event"})
@SpringBootApplication
public class EventManagementApplicationStarter {

    public static void main(String[] args) {
        SpringApplication.run(EventManagementApplicationStarter.class, args);
    }
}
