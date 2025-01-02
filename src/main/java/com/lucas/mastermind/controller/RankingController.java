package com.lucas.mastermind.controller;

import com.lucas.mastermind.DTO.TheBestThreeDTO;
import com.lucas.mastermind.service.RankingService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/ranking")
@CrossOrigin(origins = "http://localhost:3000")
public class RankingController {
    @Autowired
    RankingService rankingService;

    @Transactional
    @GetMapping("/thebestthree")
    public ResponseEntity<TheBestThreeDTO> getTheBestThree(){
        TheBestThreeDTO theBestThree = rankingService.getTheBestThree();
        if (theBestThree != null) {
            System.out.println(theBestThree);
            return new ResponseEntity<>(theBestThree, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
