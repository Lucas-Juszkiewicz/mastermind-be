package com.lucas.mastermind.controller;

import DTO.GameInProgressDTO;
import com.lucas.mastermind.entity.GameInProgress;
import com.lucas.mastermind.service.GameInProgressService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/gameinprogress")
@CrossOrigin(origins = "http://localhost:3000")
public class GameInProgressController {

    @Autowired
    GameInProgressService gipService;

    @GetMapping("/start/{userId}")
    public ResponseEntity<GameInProgress> getSequenceAndStart(@PathVariable Long userId){
        GameInProgress gameInProgress = new GameInProgress(userId);
        GameInProgress savedGameInProgress = gipService.saveGameInProgress(gameInProgress);
        return new ResponseEntity<>(savedGameInProgress, HttpStatus.OK);
    }

    @PostMapping("/check")
    public ResponseEntity<GameInProgressDTO> postResponseAfterGuess(@RequestBody GameInProgressDTO gameInProgressDTO){
        GameInProgressDTO responseAfterGuess = gipService.getResponseAfterGuess(gameInProgressDTO);
        return new ResponseEntity<>(responseAfterGuess, HttpStatus.OK);
    }
}
