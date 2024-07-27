package com.lucas.mastermind.controller;

import com.lucas.mastermind.DTO.GameInProgressDTO;
import com.lucas.mastermind.entity.GameInProgress;
import com.lucas.mastermind.service.GameInProgressService;
import com.lucas.mastermind.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/gameinprogress")
@CrossOrigin(origins = "http://localhost:3000")
public class GameInProgressController {

    @Autowired
    GameInProgressService gipService;

    @Autowired
    UserService userService;

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
    public ResponseEntity<GameInProgress> getSequenceAndStart(@AuthenticationPrincipal OidcUser principal ){
        String nickName = principal.getNickName();
        Long userId = userService.getUserDetailsByNick(nickName).getId();
        System.out.println(userId);
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
        return new ResponseEntity<>(responseAfterGuess, HttpStatus.OK);
    }

}
