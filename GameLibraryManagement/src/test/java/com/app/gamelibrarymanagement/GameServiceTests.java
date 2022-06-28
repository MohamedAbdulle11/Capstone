package com.app.gamelibrarymanagement;

import com.app.gamelibrarymanagement.model.Game;
import com.app.gamelibrarymanagement.repository.GameRepository;
import com.app.gamelibrarymanagement.service.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class GameServiceTests {

    @MockBean
    private GameRepository gameRepository;

    @Autowired
    private GameService gameService;

    @Test
    public void getAllGamesTest() {

        Game game = new Game();
        game.setId(1);
        game.setName("Fifa22");
        game.setDescription("Sports");
        game.setPhoto("Fifa.jpeg");
        when(gameRepository.findAll()).thenReturn(Collections.singletonList(game));
        assertEquals(Collections.singletonList(game), gameService.getAllGames());
    }

    @Test
    public void findGameByIdTest() {
        Game game = new Game();
        game.setId(1);
        game.setName("Fifa22");
        game.setDescription("Sports");
        game.setPhoto("Fifa.jpeg");
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        assertEquals(game, gameService.findGameById(1));
    }
}
