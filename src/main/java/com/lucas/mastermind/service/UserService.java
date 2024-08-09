package com.lucas.mastermind.service;

import com.lucas.mastermind.entity.User;
import com.lucas.mastermind.exception.DuplicateEmailException;
import com.lucas.mastermind.exception.DuplicateNickException;
import com.lucas.mastermind.exception.UserNotFoundException;
import com.lucas.mastermind.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        try {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException constraintViolationException = (ConstraintViolationException) e.getCause();
                String constraintName = constraintViolationException.getConstraintName();
                System.out.println(constraintName);
                if (constraintName != null) {
                    if (constraintName.equals("users.unique_nick")) {
                        throw new DuplicateNickException("Nick '" + user.getNick() + "' is already taken.");
                    } else if (constraintName.equals("users.unique_email")) {
                        throw new DuplicateEmailException("Email '" + user.getEmail() + "' is already registered.");
                    }
                }
            }
            throw e;
        }
    }

    public User getUserById(Long userId) {
        Optional<User> userById = userRepository.findById(userId);
        return unwrapUser(userById, userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public User updateUser(Long userId, User userWithUpdate) {
        try {
            String encodedPassword = passwordEncoder.encode(userWithUpdate.getPassword());

            Optional<User> userUpdatedAndSaved = userRepository.findById(userId).map(user -> {
                user.setNick(userWithUpdate.getNick());
                user.setEmail(userWithUpdate.getEmail());
                user.setCountry(userWithUpdate.getCountry());
                user.setPassword(encodedPassword);
                user.setGames(userWithUpdate.getGames());
                user.setTotal(userWithUpdate.getTotal());
                user.setImg(userWithUpdate.getImg());
                user.setAvatar(userWithUpdate.getAvatar());
                return userRepository.save(user);
            });
            return unwrapUser(userUpdatedAndSaved, userId);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException constraintViolationException = (ConstraintViolationException) e.getCause();
                String constraintName = constraintViolationException.getConstraintName();
                if (constraintName.equals("unique_nick")) {
                    throw new DuplicateNickException("Nick '" + userWithUpdate.getNick() + "' is already taken.");
                } else if (constraintName.equals("unique_email")) {
                    throw new DuplicateEmailException("Email '" + userWithUpdate.getEmail() + "' is already registered.");
                }
            }
            throw e;
        }
    }

    public User getUserDetailsByNick(String nick) {
        if (userRepository.findByNick(nick).isPresent()) {
            return userRepository.findByNick(nick).get();
        }
        return null;
    }

    public User getUserDetailsByNick(String nick, String password){

        String[] passwordParts = password.split("=");
        String actualPassword = passwordParts.length > 1 ? passwordParts[1] : "";
        System.out.println("######################" + nick + " Pass: " + actualPassword);

        if(userRepository.findByNick(nick).isPresent()){
            User user = userRepository.findByNick(nick).get();
            if(passwordEncoder.matches(actualPassword, user.getPassword())){
                System.out.println("Is it equal? :" +passwordEncoder.matches(actualPassword, user.getPassword()));
                return user;
            }

        }
        return null;
    }

    public User getUserDetailsByEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            return userRepository.findByEmail(email).get();
        }
        return null;
    }

    public User getUserDetailsByEmail(String email, String password){
        if(userRepository.findByEmail(email).isPresent()){
            User user = userRepository.findByEmail(email).get();
            if(passwordEncoder.matches(password, user.getPassword())){
                return user;
            }
        }
        return null;
    }

    static User unwrapUser(Optional<User> user, Long userId) {
        if (user.isPresent()) return user.get();
        else throw new UserNotFoundException(userId);
    }
}