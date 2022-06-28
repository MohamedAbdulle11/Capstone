package com.app.gamelibrarymanagement.controller;

import com.app.gamelibrarymanagement.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private GameService gameService;

    /**
     * This controller is fetching all the details of games and putting it to the admin dashboard frontend.
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("allGames", gameService.getAllGames());
        return "admin-dashboard";
    }
}
