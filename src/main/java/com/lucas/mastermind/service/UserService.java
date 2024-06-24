package com.lucas.mastermind.service;

import com.lucas.mastermind.entity.User;
import com.lucas.mastermind.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User saveUser(User user){
        User savedUser = userRepository.save(user);
        return savedUser;
    }
}
