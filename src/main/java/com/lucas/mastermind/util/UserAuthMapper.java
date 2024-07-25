package com.lucas.mastermind.util;

import com.lucas.mastermind.DTO.UserAuth;
import com.lucas.mastermind.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserAuthMapper {

    public UserAuth toUserAuth(User user) {
        if (user == null) {
            return null;
        }
        return new UserAuth(
                user.getEmail(),
                user.getNick(),
                user.getId().toString()
        );
    }
}
