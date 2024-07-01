package DTO;


import lombok.Data;

@Data
public class GameInProgressDTO {

    private Long id;
    private int[] response;

    private int[] guess;
    private int round;

    public GameInProgressDTO(Long id, int[] response, int round) {
        this.id = id;
        this.response = response;
        this.round = round;
    }
}
