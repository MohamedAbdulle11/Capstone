package com.app.gamelibrarymanagement.service;

import com.app.gamelibrarymanagement.model.Game;
import com.app.gamelibrarymanagement.model.User;
import com.app.gamelibrarymanagement.repository.GameRepository;
import com.app.gamelibrarymanagement.upload.SaveUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserService userService;

    /**
     * This method is getting used to store the game in the database along with photo

     */
    public void saveGame(Game game, MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            gameRepository.save(game);
        } else {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            game.setPhoto(fileName);
            Game savedGame = gameRepository.save(game);
            String uploadDir = "game-photos/" + savedGame.getId();
            SaveUpload.saveFile(uploadDir, fileName, multipartFile);
        }

    }

    /**
     * This method is updating the game in the database.

     */
    public void updateGame(Game game, MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        game.setPhoto(fileName);

/*        Integer customerId = gameRepository.getCustomerIdFromGame(game.getId());
        if (customerId != null) {
            game.setBorrowedStatus(true);
        }*/
        Game savedGame = gameRepository.save(game);
        String uploadDir = "game-photos/" + savedGame.getId();
        SaveUpload.saveFile(uploadDir, fileName, multipartFile);
    }

    /**
     * This method is fetching all the games from the database.
     */
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public List<Game> getAllGamesForCustomer() {
        return gameRepository.findGamesByQuantityGreaterThan(0);
    }


    /**
     * This method is borrowing a specific game.
     */
    public void borrowGame(Integer gameId) {
        Optional<Game> game = gameRepository.findById(gameId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> user = userService.findUserByUsername(username);
        if (game.isPresent() && user.isPresent()) {
            if (game.get().getQuantity() <= 0) {
                return;
            }
            for (Game g : user.get().getBorrowedGamesList()
            ) {
                if (g.equals(game.get())) {
                    return;
                }
            }
            user.get().getBorrowedGamesList().add(game.get());
            userService.updateUser(user.get());

            game.get().setQuantity(game.get().getQuantity() - 1);
            gameRepository.save(game.get());
        }
    }

    /**
     * This method is releasing a specific game.
     */
    public void releaseBorrowedGame(Integer gameId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> user = userService.findUserByUsername(username);

        Optional<Game> game = gameRepository.findById(gameId);

        if (game.isPresent() && user.isPresent()) {
            user.get().getBorrowedGamesList().remove(game.get());
            userService.updateUser(user.get());
            game.get().setQuantity(game.get().getQuantity() + 1);
            gameRepository.save(game.get());

        }
    }

    /**
     * This method is finding a specific game by using a game ID
     */
    public Game findGameById(Integer gameId) {
        Optional<Game> game = gameRepository.findById(gameId);
        if (game.isPresent()) {
            return game.get();
        } else {
            throw new RuntimeException("There is no game against this game ID: " + gameId);
        }
    }

    /**
     * This method is deleting a specific game by using a game ID
     */
    public void deleteGame(Integer gameId) {
        Optional<Game> game = gameRepository.findById(gameId);
        List<User> customers = userService.getAllCustomers();


        if (game.isPresent()) {

            if (customers.size() > 0) {
                for (User customer : customers
                ) {
                    for (Game g : customer.getPurchasedGamesList()
                    ) {
                        if (g.equals(game.get())) {
                            customer.getPurchasedGamesList().remove(g);
                            userService.updateUser(customer);
                            break;
                        }
                    }
                    for (Game g : customer.getBorrowedGamesList()
                    ) {
                        if (g.equals(game.get())) {
                            customer.getBorrowedGamesList().remove(g);
                            userService.updateUser(customer);
                            break;
                        }
                    }
                }
            }

            String gameup = "game-photos/" + gameId;
            SaveUpload.deleteIMG(gameup);
            gameRepository.delete(game.get());
        } else {
            throw new RuntimeException("There is no game against this game id in the database: " + gameId);
        }
    }
}
