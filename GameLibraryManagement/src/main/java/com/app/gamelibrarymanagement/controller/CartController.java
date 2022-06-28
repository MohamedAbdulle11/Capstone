package com.app.gamelibrarymanagement.controller;

import com.app.gamelibrarymanagement.service.CartService;
import com.app.gamelibrarymanagement.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    GameService gameService;

    /**
     * This controller is getting used to add the products in the cart.
     * It is accepting product id which we have to add in the cart.

     */
    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable(name = "id") Integer productId, Model model) {
        boolean already = cartService.addToCart(productId);
        model.addAttribute("already", already);
        model.addAttribute("allGames", gameService.getAllGamesForCustomer());
        model.addAttribute("cartItems", cartService.viewCart());

        if (already) {
            return "customer-dashboard";
        } else {
            return "redirect:/cart/view";
        }
    }

    /**
     * This controller is getting used to view the cart page.
     * It will show the cart page and all the products that are added in the cart yet.
     */
    @GetMapping("/view")
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.viewCart());
        return "cart";
    }

    @GetMapping("/delete/{id}")
    public String deleteGameFromCart(@PathVariable(name = "id") Integer gameId) {
        cartService.deleteFromCart(gameId);
        return "redirect:/cart/view";
    }

    /**
     * This is getting used for the final checkout where after finishing shopping,
     * When the user will click on checkout button, this api will get the hit.
     */
    @GetMapping("/checkout")
    public String checkout(Model model) {
        if (cartService.viewCart().isEmpty()) {
            model.addAttribute("emptyCart", true);
            return "cart";
        } else {
            model.addAttribute("allGames", cartService.checkout());
            model.addAttribute("purchased", true);
            return "my-purchased-games";
        }
    }
}
