package com.lucas.mastermind.service;

import com.lucas.mastermind.entity.User;
import com.lucas.mastermind.exception.UserNotFoundException;
import com.lucas.mastermind.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User saveUser(User user){
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public User getUserById(Long userId){
        Optional<User> userById = userRepository.findById(userId);
        return unwrapUser(userById, userId);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void deleteUser(Long userId){
        userRepository.deleteById(userId);
    }

    static User unwrapUser(Optional<User> user, Long userId){
        if(user.isPresent()) return user.get();
        else throw new UserNotFoundException(userId);
    }
}

