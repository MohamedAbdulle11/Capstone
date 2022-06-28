package com.app.gamelibrarymanagement.controller;

import com.app.gamelibrarymanagement.model.User;
import com.app.gamelibrarymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AuthenticationController {

    @Autowired
    private UserService userService;

    /**
     * This api will show the register page to the User.
     */
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * This api is accepting the registration details from the registration form and sending to the service.

     */

    @PostMapping("/register")
    public String registerForSubmission(@Valid User user, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            Optional<User> userWithSameUsername = userService.findUserByUsername(user.getUsername());
            Optional<User> userWithSameEmail = userService.findUserByEmail(user.getEmail());
            if (userWithSameUsername.isPresent()) {
                model.addAttribute("alreadyUsernameExists", true);
            } else if (userWithSameEmail.isPresent()) {
                model.addAttribute("alreadyEmailExists", true);
            } else {
                userService.saveUser(user);
                model.addAttribute("successfully", true);
                model.addAttribute("user", new User());
            }
        }
        return "register";
    }

    /**
     * This api is rendering the login page
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * This api will be used to show errors on the frontend if happens.
     */
    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("error", true);
        return "login";
    }

    /**
     * After successfully login, the request will come to this controller.
     * Here we will identify if the logged in user is admin or customer.
     * If the logged-in user is customer then he will be redirected to the customer dashboard.
     * else he will be redirected towards admin dashboard.

     */
    @GetMapping("/login-success")
    public String loginSuccess(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> user = userService.findUserByUsername(username);
        if (user.isPresent() && user.get().getRole().getName().equalsIgnoreCase("ROLE_ADMIN")) {
            return "redirect:/admin/dashboard";
        } else if (user.isPresent() && user.get().getRole().getName().equalsIgnoreCase("ROLE_CUSTOMER")) {
            return "redirect:/customer/dashboard";
        } else {
            model.addAttribute("error", true);
            return "login";
        }
    }
}
