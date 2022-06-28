package com.app.gamelibrarymanagement.controller;

import com.app.gamelibrarymanagement.model.User;
import com.app.gamelibrarymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    /**
     * This is a /home route, which is rendering our index page.
     * @return
     */
    @GetMapping("/home")
    public String home() {
        return "index";
    }


    /**
     * This is the homepage request, that is redirecting the user on the base of their role.
     * Whenever a user will open / or root url, then this api will get hit, and will identity.
     * If a user is logged in then what role he has, and he will be redirected.
     * @return
     */
    @GetMapping("/")
    public String homepage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> user = userService.findUserByUsername(username);
        if (user.isPresent() && user.get().getRole().getName().equalsIgnoreCase("ROLE_ADMIN")) {
            return "redirect:/admin/dashboard";
        } else if (user.isPresent() && user.get().getRole().getName().equalsIgnoreCase("ROLE_CUSTOMER")) {
            return "redirect:/customer/dashboard";
        } else {
            return "redirect:/home";
        }
    }
}
