package com.lucas.mastermind.controller;

import com.lucas.mastermind.entity.Game;
import com.lucas.mastermind.service.GameInProgressService;
import com.lucas.mastermind.service.GameService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@RestController
@RequestMapping("/game")
@CrossOrigin(origins = "http://localhost:3000")
public class GameController {
    @Autowired
    GameService gameService;
    @Autowired
    GameInProgressService gipService;

    @Transactional
    @GetMapping("/finishvictory/{gameId}")
    public ResponseEntity<Game> getFinishVictory(@PathVariable Long gameId){
        Boolean isSuccess = true;
        Game finishVictoryResponse = gameService.finish(gameId, isSuccess);
        gipService.deleteGameInProgress(gameId);
        if (finishVictoryResponse.getStartTime().isBefore(LocalDateTime.now()) && finishVictoryResponse.getDuration()>0) {
            return new ResponseEntity<>(finishVictoryResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @Transactional
    @GetMapping("/finishzero/{gameId}")
    public ResponseEntity<Game> getFinishZero(@PathVariable Long gameId){
        Boolean isSuccess = false;
        Game finishZeroResponse = gameService.finish(gameId, isSuccess);
        gipService.deleteGameInProgress(gameId);
        if (finishZeroResponse.getStartTime().isBefore(LocalDateTime.now()) && finishZeroResponse.getDuration()>0) {
            return new ResponseEntity<>(finishZeroResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @Transactional
    @GetMapping("/finishrounds/{gameId}")
    public ResponseEntity<Game> getFinishRounds(@PathVariable Long gameId){
        Boolean isSuccess = false;
        Game finishRoundsResponse = gameService.finish(gameId, isSuccess);
        gipService.deleteGameInProgress(gameId);
        if (finishRoundsResponse.getStartTime().isBefore(LocalDateTime.now()) && finishRoundsResponse.getDuration()>0) {
            return new ResponseEntity<>(finishRoundsResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
