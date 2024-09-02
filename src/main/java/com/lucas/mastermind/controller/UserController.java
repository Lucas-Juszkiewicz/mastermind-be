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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
@CrossOrigin("http://localhost:3000")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    GameInProgressService gameInProgressService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserAuthMapper userAuthMapper;

//    @PostMapping(value = "/save", produces = "application/json")
    @PostMapping("/save")
    public ResponseEntity<UserDTO> saveUser(@Valid @RequestBody User user) {

        User savedUser = userService.saveUser(user);

        UserDTO userDTO = userMapper.toUserDTO(savedUser);
        System.out.println(userDTO);
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
public UserAuth getUserByNickOrEmail(@PathVariable("username") String username){
    UserAuth userAuthByNick = userAuthMapper.toUserAuth(userService.getUserDetailsByNick(username));
    UserAuth userAuthByEmail = userAuthMapper.toUserAuth(userService.getUserDetailsByEmail(username));

    if(userAuthByNick != null){
        System.out.println("Username to find by: " + username);
        System.out.println("User to convert: " + userService.getUserDetailsByNick(username));
        System.out.println("User from 8081: " + userAuthByNick);
        return userAuthByNick;
    }else if(userAuthByEmail != null){
        System.out.println("Username to find by: " + username);
        System.out.println("User to convert: " + userService.getUserDetailsByEmail(username));
        System.out.println("User from 8081: " + userAuthByEmail);
        return userAuthByEmail;
    }
        return null;
}

    @GetMapping("/checkifexists")
    public ResponseEntity<UserDTO> checkUserByNickOrEmail(@AuthenticationPrincipal Jwt jwt){
        String nickName = jwt.getClaim("preferred_username");
        String email = jwt.getClaim("email");
        String subAsPassword = jwt.getClaim("sub");
        boolean existsByNick = userService.checkIfExists(nickName);
        boolean existsByEmail = userService.checkIfExists(email);

        if(existsByNick || existsByEmail){
            if(existsByNick){
                User userDetailsByNick = userService.getUserDetailsByNick(nickName);
                UserDTO userDTO = userMapper.toUserDTO(userDetailsByNick);
                return new ResponseEntity<>( userDTO, HttpStatus.OK);
            }else{
                User userDetailsByEmail = userService.getUserDetailsByEmail(email);
                UserDTO userDTO = userMapper.toUserDTO(userDetailsByEmail);
                return new ResponseEntity<>( userDTO, HttpStatus.OK);
            }
        }else{
            User user = new User(nickName, email, subAsPassword);
            User userSaved = userService.saveUser(user);
            UserDTO userDTO = userMapper.toUserDTO(userSaved);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
    }


@PostMapping("/{username}/verify-password")
VerifyPasswordResponse verifyUserPassword(@PathVariable("username") String username, @RequestBody String password){
        VerifyPasswordResponse returnValue = new VerifyPasswordResponse(false);

    User userDetailsByNick = userService.getUserDetailsByNick(username, password);
    User userDetailsByEmail = userService.getUserDetailsByEmail(username, password);
    if(userDetailsByNick != null){
        returnValue.setResult(true);
    }else if(userDetailsByEmail != null){
        returnValue.setResult(true);
    }

    System.out.println("UserDetailsByNickOrEmail: " + userDetailsByNick);
    return returnValue;
}
}
