package com.app.gamelibrarymanagement.service;

import com.app.gamelibrarymanagement.model.Game;
import com.app.gamelibrarymanagement.model.User;
import com.app.gamelibrarymanagement.repository.GameRepository;
import com.app.gamelibrarymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    HashMap<Integer, Game> gameHashMap = new HashMap<>();

    @Autowired
    GameRepository gameRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * This function is adding the products in the cart.
     */
    public boolean addToCart(Integer gameId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> user = userRepository.findByUsername(username);

        Optional<Game> game = gameRepository.findById(gameId);
        if (game.isPresent() && user.isPresent()) {

            for (Game g : user.get().getPurchasedGamesList()
            ) {
                if (g.equals(game.get())) {
                    return true;
                }
            }
            if (!gameHashMap.containsKey(game.get().getId())) {
                gameHashMap.put(game.get().getId(), game.get());
                return false;
            } else {
                return true;
            }
        } else {
            throw new RuntimeException("Game not found against this id:" + gameId);
        }
    }

    public void deleteFromCart(Integer gameId) {
        if (gameHashMap.containsKey(gameId)) {
            gameHashMap.remove(gameId);
        } else {
            throw new RuntimeException("There is no product in the cart against this ID:" + gameId);
        }
    }

    /**
     * This function is getting used to combine the things to view the cart.
     */
    public List<Game> viewCart() {
        List<Game> games = new ArrayList<>();
        gameHashMap.forEach(
                (key, value) -> games.add(value));
        return games;
    }

    /**
     * This is a checkout function where all the checkout business logic is written.
     */
    public List<Game> checkout() {
        List<Game> games = viewCart();

        for (Game game : games
        ) {
            Optional<Game> dbGame = gameRepository.findById(game.getId());
            if (dbGame.isPresent()) {
                dbGame.get().setQuantity(dbGame.get().getQuantity() - 1);
                gameRepository.save(dbGame.get());
                game.setQuantity(game.getQuantity() - 1);
            }
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            user.get().getPurchasedGamesList().addAll(games);
            userRepository.save(user.get());
            gameHashMap.clear();
            return user.get().getPurchasedGamesList();
        } else {
            throw new RuntimeException("User doesn't exists in the database with this username");
        }
    }
}
