package com.lucas.mastermind.service;

import com.lucas.mastermind.entity.User;
import com.lucas.mastermind.exception.DuplicateNickException;
import com.lucas.mastermind.exception.UserNotFoundException;
import com.lucas.mastermind.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User saveUser(User user){
        try {
            User savedUser = userRepository.save(user);
            return savedUser;
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new DuplicateNickException("Nick '" + user.getNick() + "' is already taken.");
            }
            throw e;
        }
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

    public User updateUser(Long userId, User userWithUpdate) {
        try {
            Optional<User> userUpdatedAndSaved = userRepository.findById(userId).map(user -> {
                user.setNick(userWithUpdate.getNick());
                user.setEmail(userWithUpdate.getEmail());
                user.setCountry(userWithUpdate.getCountry());
                user.setPassword(userWithUpdate.getPassword());
                user.setGames(userWithUpdate.getGames());
                user.setTotal(userWithUpdate.getTotal());
                user.setImg(userWithUpdate.getImg());
                user.setAvatar(userWithUpdate.getAvatar());
                return userRepository.save(user);
            });
            return unwrapUser(userUpdatedAndSaved, userId);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new DuplicateNickException("Nick '" + userWithUpdate.getNick() + "' is already taken.");
            }
            throw e;
        }
    }

    static User unwrapUser(Optional<User> user, Long userId){
        if(user.isPresent()) return user.get();
        else throw new UserNotFoundException(userId);
    }
}

