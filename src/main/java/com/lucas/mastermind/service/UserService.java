package com.lucas.mastermind.service;

import com.lucas.mastermind.entity.User;
import com.lucas.mastermind.exception.DuplicateEmailException;
import com.lucas.mastermind.exception.DuplicateNickException;
import com.lucas.mastermind.exception.UserNotFoundException;
import com.lucas.mastermind.repository.GameRepository;
import com.lucas.mastermind.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    GameRepository gameRepository;

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
//        Long numberOfGamesValue;
//        if(gameRepository.countByUserId(userWithUpdate.getId()) == null){
//            numberOfGamesValue = 0L;
//        }else{
//            numberOfGamesValue = gameRepository.countByUserId(userWithUpdate.getId());
//        }
//        Long numberOfGames = numberOfGamesValue;

        try {
//            String encodedPassword = passwordEncoder.encode(userWithUpdate.getPassword());
            User user = unwrapUser(userRepository.findById(userId), userId);
            if (!Objects.equals(userWithUpdate.getEmail(), "")) {
                user.setEmail(userWithUpdate.getEmail());
            }
            if (!Objects.equals(userWithUpdate.getCountry(), "")) {
                user.setCountry(userWithUpdate.getCountry());
            }
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException constraintViolationException = (ConstraintViolationException) e.getCause();
                String constraintName = constraintViolationException.getConstraintName();
                System.out.println(constraintName);
                if (constraintName != null) {
                    if (constraintName.equals("users.unique_email")) {
                        throw new DuplicateEmailException("Email '" + userWithUpdate.getEmail() + "' is already registered.");
                    }
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

    public User getUserDetailsByNick(String nick, String password) {
        String[] passwordParts;
        String actualPassword;
if(password.contains("=")){
    passwordParts = password.split("=");
    actualPassword = passwordParts.length > 1 ? passwordParts[1] : "";
}else{
    actualPassword=password;
}
        System.out.println("getUserDetailsByNick - userService: ");
        System.out.println(("nick:" + nick));
        System.out.println("password:" + password);
        System.out.println("######################" + nick + " Pass:" + actualPassword);

        if (userRepository.findByNick(nick).isPresent()) {
            User user = userRepository.findByNick(nick).get();

            System.out.println("UserByNick from DB:");
            System.out.println("nick:" + user.getNick());
            System.out.println("email:" + user.getEmail());
            System.out.println("password:" + user.getPassword());

            System.out.println("Is it equal? :" + passwordEncoder.matches(actualPassword, user.getPassword()));
            if (passwordEncoder.matches(actualPassword, user.getPassword())) {
                System.out.println("Is it equal? :" + passwordEncoder.matches(actualPassword, user.getPassword()));
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

    public User getUserDetailsByEmail(String email, String password) {
        String[] passwordParts;
        String actualPassword;
        if(password.contains("=")){
            passwordParts = password.split("=");
            actualPassword = passwordParts.length > 1 ? passwordParts[1] : "";
        }else{
            actualPassword=password;
        }

        System.out.println("getUserDetailsByEmail - userService: ");
        System.out.println(("Email:" + email));
        System.out.println("password:" + password);
        System.out.println("######################" + email + " Pass:" + actualPassword);

        if (userRepository.findByEmail(email).isPresent()) {
            User user = userRepository.findByEmail(email).get();

            System.out.println("UserByNick from DB:");
            System.out.println("nick:" + user.getNick());
            System.out.println("email:" + user.getEmail());
            System.out.println("password:" + user.getPassword());
            System.out.println("Is it equal? :" + passwordEncoder.matches(actualPassword, user.getPassword()));
            if (passwordEncoder.matches(actualPassword, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    public boolean checkIfExists(String emailOrNick) {
        boolean existsByEmail = userRepository.existsByEmail(emailOrNick);
        boolean existsByNick = userRepository.existsByNick(emailOrNick);

        return existsByEmail || existsByNick;
    }

    static User unwrapUser(Optional<User> user, Long userId) {
        if (user.isPresent()) return user.get();
        else throw new UserNotFoundException(userId);
    }
}