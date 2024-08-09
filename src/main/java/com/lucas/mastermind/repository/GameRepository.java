package com.lucas.mastermind.repository;

import com.lucas.mastermind.entity.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
    @Override
    Game save(Game game);
}
