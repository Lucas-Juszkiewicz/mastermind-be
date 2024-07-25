package com.lucas.mastermind.controller;

import com.lucas.mastermind.DTO.UserAuth;
import com.lucas.mastermind.DTO.UserDTO;
import com.lucas.mastermind.entity.User;
import com.lucas.mastermind.service.GameInProgressService;
import com.lucas.mastermind.service.UserService;
import com.lucas.mastermind.util.UserAuthMapper;
import com.lucas.mastermind.util.UserMapper;
import com.lucas.mastermind.util.VerifyPasswordResponse;
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

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserAuthMapper userAuthMapper;

    @PostMapping("/save")
    public ResponseEntity<UserDTO> saveUser(@Valid @RequestBody User user) {

        User savedUser = userService.saveUser(user);

        UserDTO userDTO = userMapper.toUserDTO(savedUser);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody User userWithUpdate) {
        User updatedUser = userService.updateUser(id, userWithUpdate);

        UserDTO userDTO = userMapper.toUserDTO(updatedUser);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);


    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        User userById = userService.getUserById(userId);

        UserDTO userDTO = userMapper.toUserDTO(userById);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserDTO>> getAllUsers() {

        List<UserDTO> userDTOs = userService.getAllUsers().stream()
                .map(user -> userMapper.toUserDTO(user))
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

//    @GetMapping("/{userEmail}")
//    public UserAuth getUserByEmail(@PathVariable String email){
//        return userAuthMapper.toUserAuth(userService.getUserDetailsByEmail(email));
//    }


//    @PostMapping("/{userEmail}/verify-password")
//    VerifyPasswordResponse verifyUserPassword(@PathVariable String email, @RequestBody String password){
//        VerifyPasswordResponse returnValue = new VerifyPasswordResponse(false);
//
//        User userDetailsByEmail = userService.getUserDetailsByEmail(email, password);
//        if(userDetailsByEmail != null){
//            returnValue.setResult(true);
//        }
//        return returnValue;
//    }


@GetMapping("/{username}")
public UserAuth getUserByNick(@PathVariable("username") String username){
    UserAuth userAuth = userAuthMapper.toUserAuth(userService.getUserDetailsByNick(username));
    System.out.println("Username to find by: " + username);
    System.out.println("User to convert: " + userService.getUserDetailsByNick(username));
    System.out.println("User from 8081: " + userAuth);
        return userAuth;
}


@PostMapping("/{username}/verify-password")
VerifyPasswordResponse verifyUserPassword(@PathVariable("username") String username, @RequestBody String password){
        VerifyPasswordResponse returnValue = new VerifyPasswordResponse(false);

    User userDetailsByNick = userService.getUserDetailsByNick(username, password);
    if(userDetailsByNick != null){
        returnValue.setResult(true);
    }
    System.out.println("UserDetailsByNick: " + userDetailsByNick);
    return returnValue;
}
}
