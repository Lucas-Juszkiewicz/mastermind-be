package com.lucas.mastermind.controller;

import DTO.UserDTO;
import com.lucas.mastermind.entity.User;
import com.lucas.mastermind.service.GameInProgressService;
import com.lucas.mastermind.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    GameInProgressService gameInProgressService;

    @PostMapping("/save")
    public ResponseEntity<UserDTO> saveUser(@Valid @RequestBody User user) {

        User savedUser = userService.saveUser(user);

        UserDTO userDTO = new UserDTO(savedUser.getId(), savedUser.getNick(), savedUser.getEmail(), savedUser.getCountry(), savedUser.getTotal(), savedUser.getImg(), savedUser.getAvatar(), savedUser.getRegistrationDate());

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody User userWithUpdate) {
        User updatedUser = userService.updateUser(id, userWithUpdate);

        UserDTO userDTO = new UserDTO(updatedUser.getId(), updatedUser.getNick(), updatedUser.getEmail(), updatedUser.getCountry(), updatedUser.getTotal(), updatedUser.getImg(), updatedUser.getAvatar(), updatedUser.getRegistrationDate());

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);


    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        User userById = userService.getUserById(userId);

        UserDTO userDTO = new UserDTO(userById.getId(), userById.getNick(), userById.getEmail(), userById.getCountry(), userById.getTotal(), userById.getImg(), userById.getAvatar(), userById.getRegistrationDate());

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserDTO>> getAllUsers() {

        List<UserDTO> userDTOs = userService.getAllUsers().stream()
                .map(user -> new UserDTO(user.getId(), user.getNick(), user.getEmail(), user.getCountry(), user.getTotal(), user.getImg(), user.getAvatar(), user.getRegistrationDate()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }


    @DeleteMapping("/delete/{userId}")
    @Transactional
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable Long userId) {
        gameInProgressService.deleteByUserId(userId);
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
