package com.lucas.mastermind.util;

import com.lucas.mastermind.DTO.UserDTO;
import com.lucas.mastermind.entity.User;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class UserMapper {



    public UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }
       String imgAsString;
        byte[] img = user.getImg();
if(img != null){
    imgAsString = Base64.getEncoder().encodeToString(img);
}else{
    imgAsString = null;
}

        return new UserDTO(
                user.getId(),
                user.getNick(),
                user.getEmail(),
                user.getCountry(),
                user.getTotal(),
                imgAsString,
                user.getAvatar(),
                user.getRegistrationDate(),
                user.getNumberOfGames()
        );
    }
}