package DTO;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameInProgressDTO {

    private Long id;
    private int[] response;

    private int[] guess;
    private int round;
    private String finalMessage;

    public GameInProgressDTO(Long id, int[] response, int round) {
        this.id = id;
        this.response = response;
        this.round = round;
    }

    public GameInProgressDTO(Long id, int[] response, int round, String finalMessage) {
        this.id = id;
        this.response = response;
        this.round = round;
        this.finalMessage = finalMessage;
    }
}