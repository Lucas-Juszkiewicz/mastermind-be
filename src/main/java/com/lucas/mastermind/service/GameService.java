package com.lucas.mastermind.service;

import com.lucas.mastermind.entity.Game;
import com.lucas.mastermind.entity.User;
import com.lucas.mastermind.repository.GameRepository;
import com.lucas.mastermind.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class GameService {
    @Autowired
    GameRepository gameRepository;

    @Autowired
    UserService userService;
public Game saveGame(Game game, Long userId){
    User user = userService.getUserById(userId);
    game.setUser(user);
    return gameRepository.save(game);
}

}
