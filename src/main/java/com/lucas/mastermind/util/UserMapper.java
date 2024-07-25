package com.lucas.mastermind.util;

import com.lucas.mastermind.DTO.UserDTO;
import com.lucas.mastermind.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
                user.getId(),
                user.getNick(),
                user.getEmail(),
                user.getCountry(),
                user.getTotal(),
                user.getImg(),
                user.getAvatar(),
                user.getRegistrationDate()
        );
    }
}