package com.app.gamelibrarymanagement.controller;

import com.app.gamelibrarymanagement.model.Game;
import com.app.gamelibrarymanagement.service.CartService;
import com.app.gamelibrarymanagement.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private CartService cartService;

    //================= add game section =================//

    /**
     * This api is helping us to view the form to add a game.
     *
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String addGame(Model model) {
        model.addAttribute("game", new Game());
        return "add-game";
    }

    /**
     * This api is accepting the new game object from frontend and sending to our service class to store it in the db.
     *
     * @param game
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @PostMapping("/add")
    public String addGame(Game game, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        gameService.saveGame(game, multipartFile);
        return "redirect:/admin/dashboard";
    }

    //================= Borrowed or available games section =================//

/*    *//**
     * This Api is helping us to fetch the list of all borrowed games.
     *
     * @param model
     * @return
     *//*
    @GetMapping("/borrowed")
    public String getAllBorrowedGames(Model model) {
        model.addAttribute("allGames", gameService.getAllBorrowedGames());
        model.addAttribute("appendMessage", "Borrowed");
        return "games-list";
    }

    *//**
     * This Api is helping us to fetch the list of all un-borrowed games.
     *
     * @param model
     * @return
     */

    /**
     * This Api is helping us to borrow a specific game.
     * Customer have this button to borrow a game where he clicks on borrow button, and this api gets hit.

     */
    @GetMapping("/borrow/{id}")
    public String borrowGame(Model model, @PathVariable(name = "id") Integer gameId) {
        gameService.borrowGame(gameId);
        model.addAttribute("borrowed", true);
        model.addAttribute("allGames", gameService.getAllGamesForCustomer());
        model.addAttribute("cartItems", cartService.viewCart());
        return "customer-dashboard";
    }

    /**
     * This Api is helping us to release a specific game.
     * Customer have this button to release a game where he clicks on release button, and this api gets hit.
     *
     * @param model
     * @param gameId
     * @return
     */
    @GetMapping("/release/{id}")
    public String releaseGame(Model model, @PathVariable(name = "id") Integer gameId) {
        gameService.releaseBorrowedGame(gameId);
        model.addAttribute("released", true);
        model.addAttribute("allGames", gameService.getAllGamesForCustomer());
        model.addAttribute("cartItems", cartService.viewCart());
        return "customer-dashboard";
    }

    /**
     * This Api is helping us to update a specific game.
     *
     * @param gameId
     * @param model
     * @return
     */
    @GetMapping("/update/{id}")
    public String updateGame(@PathVariable(name = "id") Integer gameId, Model model) {
        model.addAttribute("game", gameService.findGameById(gameId));
        return "update-game";
    }

    /**
     * This Api is helping us to update a specific game.
     * This api is accepting the request from update form from the frontend.

     */
    @PostMapping("/update")
    public String updateGame(Game game, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        gameService.updateGame(game, multipartFile);
        return "redirect:/admin/dashboard";
    }


    /**
     * This Api is helping us to delete a specific game.
     *

     */
    @GetMapping("/delete/{id}")
    public String deleteGame(@PathVariable(name = "id") Integer gameId) throws IOException {
        gameService.deleteGame(gameId);
        return "redirect:/admin/dashboard";
    }
}
