package com.lucas.mastermind.service;

import com.lucas.mastermind.DTO.TheBestThreeDTO;
import com.lucas.mastermind.DTO.UserDTO;
import com.lucas.mastermind.util.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RankingService {
    @Autowired
    UserService userService;

    UserMapper userMapper;

    public TheBestThreeDTO getTheBestThree() {

//        List<User> top3UsersByTotal = userService.getTop3UsersByTotal();
//        List<UserDTO> top3UsersDTO = top3UsersByTotal.stream().sorted(Comparator.comparing(a -> a.getTotal() / a.getNumberOfGames()).reversed()).map(a -> userMapper.toUserDTO(a)).collect(Collectors.toList());

        if (!userService.getTop3UsersByTotal().isEmpty()) {
            List<UserDTO> listOfTheBestThree = userService.getTop3UsersByTotal().stream()
                    .map(user -> userMapper.toUserDTO(user))  // map User to UserDTO
                    .toList();

            List<UserDTO> sortedBestThree = listOfTheBestThree.stream()
                    .sorted(Comparator.comparingDouble(this::computeAvg).reversed())
                    .collect(Collectors.toList());

            TheBestThreeDTO response = new TheBestThreeDTO();
            for (int i = 0; i < sortedBestThree.size(); i++) {
                switch (i) {
                    case 0 -> response.setFirst(sortedBestThree.get(i));
                    case 1 -> response.setSecond(sortedBestThree.get(i));
                    case 2 -> response.setThird(sortedBestThree.get(i));
                }
            }

            return response;
        }
        return null;
    }

    double computeAvg(UserDTO user) {
        Long total = user.getTotal();
        Long numberOfGames = user.getNumberOfGames();
        if (total != 0 && numberOfGames != 0) {
            return (double) total / numberOfGames;
        } else {
            return 0;
        }
    }
}
