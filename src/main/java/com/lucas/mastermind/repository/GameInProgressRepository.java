package com.lucas.mastermind.repository;

import com.lucas.mastermind.entity.GameInProgress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GameInProgressRepository extends CrudRepository<GameInProgress, Long> {
    GameInProgress findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
