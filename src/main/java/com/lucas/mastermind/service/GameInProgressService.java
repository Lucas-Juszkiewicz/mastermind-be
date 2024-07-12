package com.lucas.mastermind.service;

import DTO.GameInProgressDTO;
import com.lucas.mastermind.entity.Game;
import com.lucas.mastermind.entity.GameInProgress;
import com.lucas.mastermind.exception.GameInProgressNotFoundException;
import com.lucas.mastermind.repository.GameInProgressRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

@AllArgsConstructor
@Service
public class GameInProgressService {
    @Autowired
    GameInProgressRepository gipRepository;

    @Autowired
    GameService gameService;

    public GameInProgress saveGameInProgress(GameInProgress gameInProgress) {
        return gipRepository.save(gameInProgress);
    }


    public GameInProgressDTO getResponseAfterGuess(GameInProgressDTO gameInProgressDTO) {
        int[] guess = gameInProgressDTO.getGuess();
        Long gameInProgressId = gameInProgressDTO.getId();
        GameInProgressDTO responseAfterGuess = new GameInProgressDTO();
        String finalMessage;

        //Round Update
        if (gipRepository.findById(gameInProgressId).isPresent()) {
            GameInProgress gameInProgress = gipRepository.findById(gameInProgressId).get();
            int round = gameInProgress.getRound();
            finalMessage = "Game is over.";
            responseAfterGuess.setFinalMessage(finalMessage);
            gameInProgress.addGuess(round, guess);
            gameInProgress.setRound(round + 1);
            gipRepository.save(gameInProgress);

            responseAfterGuess = checkSequence(guess, gameInProgressId);
            int[] response = responseAfterGuess.getResponse();
            if (response[0] == 8) {
                finalMessage = "win";
            } else if (round + 1 == 12) {
                finalMessage = "defeat";
            }
            responseAfterGuess.setFinalMessage(finalMessage);


            int[][] previousResponses = gameInProgress.getPreviousResponses();
            previousResponses[round] = responseAfterGuess.getResponse();

            gameInProgress.addSinglePreviousResponses(round, previousResponses);
            gipRepository.save(gameInProgress);

            GameInProgress gameInProgressUpdated = gipRepository.findById(gameInProgressId).get();
            responseAfterGuess.setPreviousGuesses(gameInProgress.getGuesses());
            responseAfterGuess.setPreviousResponses(gameInProgressUpdated.getPreviousResponses());
        }
        return responseAfterGuess;
    }


    public ResponseEntity<HttpStatus> deleteGameInProgress(Long gameId){
        try {
            gipRepository.deleteById(gameId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private GameInProgressDTO checkSequence(int[] guess, Long gameInProgressId) {
        if (gipRepository.findById(gameInProgressId).isPresent()) {
            GameInProgress gameInProgress = gipRepository.findById(gameInProgressId).get();
            int[] sequence = gameInProgress.getSequence();
            int round = gameInProgress.getRound();
            int[] response = new int[2];
            int[] sequenceCount = new int[9];
            int[] guessCount = new int[9];
            int green = 0;
            int commonCount = 0;

            for (int i = 0; i < 8; i++) {
                if (sequence[i] == guess[i]) {
                    green++;
                }
            }

            for (int i = 0; i < 9; i++) {
                int b = i + 1;
                int countSeq = (int) Arrays.stream(sequence).filter(a -> a == b).count();
                sequenceCount[i] = countSeq;

                int countGuess = (int) Arrays.stream(guess).filter(a -> a == b).count();
                guessCount[i] = countGuess;
            }

            for (int i = 0; i < 9; i++) {
                if (sequenceCount[i] == guessCount[i]) {
                    commonCount = commonCount + sequenceCount[i];
                } else {
                    if (sequenceCount[i] > guessCount[i]) {
                        commonCount = commonCount + guessCount[i];
                    }
                    if (sequenceCount[i] < guessCount[i]) {
                        commonCount = commonCount + sequenceCount[i];
                    }
                }
            }

            int yellow = commonCount - green;
            response[0] = green;
            response[1] = yellow;
            return new GameInProgressDTO(gameInProgressId, response, round);
        } else {
            throw new GameInProgressNotFoundException(gameInProgressId);
        }
    }
}
