package com.app.gamelibrarymanagement;

import com.app.gamelibrarymanagement.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GameLibraryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameLibraryManagementApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            userService.createNewAdminOnRuntime();
            userService.createNewCustomersOnRuntime();
        };
    }
}
