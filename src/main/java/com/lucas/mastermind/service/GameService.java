package com.lucas.mastermind.service;

import com.lucas.mastermind.entity.Game;
import com.lucas.mastermind.entity.GameInProgress;
import com.lucas.mastermind.entity.User;
import com.lucas.mastermind.repository.GameInProgressRepository;
import com.lucas.mastermind.repository.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class GameService {
    @Autowired
    GameRepository gameRepository;

    @Autowired
    UserService userService;

    @Autowired
    GameInProgressRepository gipRepository;

    public Game saveGame(Game game, Long userId) {
        User user = userService.getUserById(userId);
        game.setUser(user);
        return gameRepository.save(game);
    }

    public Game finish(Long id) {
        try {
            if (gipRepository.findById(id).isPresent()) {
                GameInProgress gameInProgress = gipRepository.findById(id).get();

                long userId = gameInProgress.getUserId();
                LocalDateTime startTime = gameInProgress.getStartTime();
                int round = gameInProgress.getRound();
                int[] sequence = gameInProgress.getSequence();
                int[][] guesses = gameInProgress.getGuesses();
                int[][] responses = gameInProgress.getPreviousResponses();

                Game game = new Game(startTime, round, sequence, guesses, responses);
                return saveGame(game, userId);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
