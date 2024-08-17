package com.lucas.mastermind.util;

import com.lucas.mastermind.DTO.GameInProgressDTO;
import com.lucas.mastermind.entity.GameInProgress;
import org.springframework.stereotype.Component;

@Component
public class GameInProgressMapper {
    public GameInProgressDTO toGameInProgressDTO(GameInProgress gameInProgress){
        if(gameInProgress == null){
            return null;
        }
        return new GameInProgressDTO(gameInProgress.getId(), gameInProgress.getPreviousResponses(), gameInProgress.getGuesses(), gameInProgress.getRound(), gameInProgress.getStartTime());
    }
}
