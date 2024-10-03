package com.lucas.mastermind.controller;

import com.lucas.mastermind.DTO.CheckIfExistsDTO;
import com.lucas.mastermind.DTO.GameInProgressDTO;
import com.lucas.mastermind.entity.GameInProgress;
import com.lucas.mastermind.service.GameInProgressService;
import com.lucas.mastermind.service.UserService;
import com.lucas.mastermind.util.GameInProgressMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/gameinprogress")
@CrossOrigin("http://localhost:3000")
public class GameInProgressController {

    @Autowired
    GameInProgressService gipService;

    @Autowired
    UserService userService;

    @Autowired
    GameInProgressMapper gipMapper;

//    @GetMapping("/start/{userId}")
//    public ResponseEntity<GameInProgress> getSequenceAndStart(@PathVariable Long userId){
//        GameInProgress gameInProgress = new GameInProgress(userId);
//        GameInProgress savedGameInProgress = gipService.saveGameInProgress(gameInProgress);
//        if(savedGameInProgress != null){
//            return new ResponseEntity<>(savedGameInProgress, HttpStatus.OK);
//        }else {
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }
//
//    }

    @GetMapping("/start")
    public ResponseEntity<GameInProgress> getSequenceAndStart(@AuthenticationPrincipal Jwt jwt){
        String nickName = jwt.getClaim("preferred_username");
        Long userId = userService.getUserDetailsByNick(nickName).getId();
        System.out.println(userId);
        System.out.println(nickName);
        GameInProgress gameInProgress = new GameInProgress(userId);
        GameInProgress savedGameInProgress = gipService.saveGameInProgress(gameInProgress);
        if(savedGameInProgress != null){
            return new ResponseEntity<>(savedGameInProgress, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

    @PostMapping("/check")
    public ResponseEntity<GameInProgressDTO> postResponseAfterGuess(@RequestBody GameInProgressDTO gameInProgressDTO){
        GameInProgressDTO responseAfterGuess = gipService.getResponseAfterGuess(gameInProgressDTO);

//        if(responseAfterGuess.getFinalMessage().equals("defeat")){
//
//        }
        return new ResponseEntity<>(responseAfterGuess, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<GameInProgressDTO> getGameAfterRecall(@AuthenticationPrincipal Jwt jwt){
        String nickName = jwt.getClaim("preferred_username");
        Long userId = userService.getUserDetailsByNick(nickName).getId();
        GameInProgress gameInProgress = gipService.findGamesInProgressByUserId(userId);
        GameInProgressDTO gameInProgressDTO = gipMapper.toGameInProgressDTO(gameInProgress);
        if(gameInProgressDTO != null){
            return new ResponseEntity<>(gameInProgressDTO, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    @GetMapping("/checkifexists")
    public ResponseEntity<CheckIfExistsDTO> checkIfGameInProgressExists(@AuthenticationPrincipal Jwt jwt){
        String nickName = jwt.getClaim("preferred_username");
        Long userId = userService.getUserDetailsByNick(nickName).getId();
        GameInProgress gameInProgress = gipService.findGamesInProgressByUserId(userId);
        CheckIfExistsDTO checkIfExists;
        if(gameInProgress != null){
            checkIfExists = new CheckIfExistsDTO(true);
        }else {
            checkIfExists = new CheckIfExistsDTO(false);
        }
        return new ResponseEntity<>(checkIfExists, HttpStatus.OK);

    }

}
