package com.app.gamelibrarymanagement.controller;

import com.app.gamelibrarymanagement.model.User;
import com.app.gamelibrarymanagement.service.CartService;
import com.app.gamelibrarymanagement.service.GameService;
import com.app.gamelibrarymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    /**
     * This is the request for admin dashboard.
     * When this api will be hit, all the games will be shown to the customer dashboard.
     * There he can borrow the games.
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("allGames", gameService.getAllGamesForCustomer());
        model.addAttribute("cartItems", cartService.viewCart());
        return "customer-dashboard";
    }

    /**
     * This api is fetching the list of all the customers that are in our system and showing on the customer-list.html page.
     * This is helping the admin to view all the customers.
     */
    @GetMapping("/list")
    public String getAllCustomers(Model model) {
        model.addAttribute("customersList", userService.getAllCustomers());
        return "customers-list";
    }

    /**
     * This api is fetching games that a customer borrowed.
     * It's for my games section for each user where they can see the games they borrowed.
     */
    @GetMapping("/borrowedGames")
    public String myGames(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> user = userService.findUserByUsername(username);
        if (user.isPresent()) {
            model.addAttribute("allGames", user.get().getBorrowedGamesList());
            return "my-borrowed-games";
        } else {
            return "redirect:/customer/dashboard";
        }
    }

    @GetMapping("/purchasedGames")
    public String myPurchasedGames(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> user = userService.findUserByUsername(username);
        if (user.isPresent()) {
            model.addAttribute("allGames", user.get().getPurchasedGamesList());
            return "my-purchased-games";
        } else {
            return "redirect:/customer/dashboard";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable(name = "id") Integer customerId) {
        userService.deleteCustomer(customerId);
        return "redirect:/customer/list";
    }
}
