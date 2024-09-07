package com.lucas.mastermind.controller;

import com.lucas.mastermind.DTO.UpdatePasswordRequest;
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
import org.springframework.web.multipart.MultipartFile;

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

    @PutMapping("/update/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String userId, @Valid @RequestBody User userWithUpdate) {
        long userIdLong = Long.parseLong(userId);
        User updatedUser = userService.updateUser(userIdLong, userWithUpdate);

        UserDTO userDTO = userMapper.toUserDTO(updatedUser);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);

    }

    @PostMapping("/uploadAvatar/{userId}")
    public ResponseEntity<UserDTO> uploadAvatar(@RequestParam("img") MultipartFile file, @PathVariable Long userId) {
        final long MAX_FILE_SIZE = 2 * 1024 * 1024;

        try {

            if (file.getSize() > MAX_FILE_SIZE) {
                return new ResponseEntity<>(HttpStatus.PAYLOAD_TOO_LARGE); // 413 Payload Too Large
            }
            // Convert the MultipartFile to byte[] and store it
            byte[] imageBytes = file.getBytes();

            // Here you would save the imageBytes to the database
            User userById = userService.getUserById(userId);
            userById.setImg(imageBytes);
            User userWithAvatar = userService.saveUser(userById);
            UserDTO userDTO = userMapper.toUserDTO(userWithAvatar);

            return new ResponseEntity<>(userDTO, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/typeOfLoginCheck")
    public String typeOfLoginCheck(@AuthenticationPrincipal Jwt jwt) {
        String nick = jwt.getClaim("preferred_username");
        String sub = jwt.getClaim("sub");

        User userByNickAndPassword = userService.getUserDetailsByNick(nick, sub);
        if (userByNickAndPassword != null) {
            return "socialLogin";
        } else {
            return "privateLogin";
        }

    }

    @PostMapping("/passUpdate")
    public ResponseEntity<HttpStatus> passUpdate(@RequestBody UpdatePasswordRequest request, @AuthenticationPrincipal Jwt jwt) {
        String nick = jwt.getClaim("preferred_username");
        String sub = jwt.getClaim("sub");
        String passOne = request.getPassOne();
        String passTwo = request.getPassTwo();

        System.out.println("AAAAAAAAAAAAAAAAAAAAA: " + request);
        System.out.println("passOne: " + passOne);
        System.out.println("passTwo: " + passTwo);

        User userByNickAndSub = userService.getUserDetailsByNick(nick, sub);
        User userByNickAndPassword = userService.getUserDetailsByNick(nick, request.getPassOld());

        if (userByNickAndSub != null) {
            userByNickAndSub.setPassword(passOne);
            userService.saveUser(userByNickAndSub);
            return new ResponseEntity<>(HttpStatus.OK);

        } else if (userByNickAndPassword != null) {
            if (passOne.equals(passTwo)) {
                userByNickAndPassword.setPassword(passOne);
                userService.saveUser(userByNickAndPassword);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
    public UserAuth getUserByNickOrEmail(@PathVariable("username") String username) {
        UserAuth userAuthByNick = userAuthMapper.toUserAuth(userService.getUserDetailsByNick(username));
        UserAuth userAuthByEmail = userAuthMapper.toUserAuth(userService.getUserDetailsByEmail(username));

        if (userAuthByNick != null) {
            System.out.println("Username to find by: " + username);
            System.out.println("User to convert: " + userService.getUserDetailsByNick(username));
            System.out.println("User from 8081: " + userAuthByNick);
            return userAuthByNick;
        } else if (userAuthByEmail != null) {
            System.out.println("Username to find by: " + username);
            System.out.println("User to convert: " + userService.getUserDetailsByEmail(username));
            System.out.println("User from 8081: " + userAuthByEmail);
            return userAuthByEmail;
        }
        return null;
    }

    @GetMapping("/checkifexists")
    public ResponseEntity<UserDTO> checkUserByNickOrEmail(@AuthenticationPrincipal Jwt jwt) {
        String nickName = jwt.getClaim("preferred_username");
        String email = jwt.getClaim("email");
        String subAsPassword = jwt.getClaim("sub");
        boolean existsByNick = userService.checkIfExists(nickName);
        boolean existsByEmail = userService.checkIfExists(email);

        if (existsByNick || existsByEmail) {
            if (existsByNick) {
                User userDetailsByNick = userService.getUserDetailsByNick(nickName);
                UserDTO userDTO = userMapper.toUserDTO(userDetailsByNick);
                return new ResponseEntity<>(userDTO, HttpStatus.OK);
            } else {
                User userDetailsByEmail = userService.getUserDetailsByEmail(email);
                UserDTO userDTO = userMapper.toUserDTO(userDetailsByEmail);
                return new ResponseEntity<>(userDTO, HttpStatus.OK);
            }
        } else {
            User user = new User(nickName, email, subAsPassword);
            User userSaved = userService.saveUser(user);
            UserDTO userDTO = userMapper.toUserDTO(userSaved);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
    }


    @PostMapping("/{username}/verify-password")
    VerifyPasswordResponse verifyUserPassword(@PathVariable("username") String username, @RequestBody String password) {
        VerifyPasswordResponse returnValue = new VerifyPasswordResponse(false);

        User userDetailsByNick = userService.getUserDetailsByNick(username, password);
        User userDetailsByEmail = userService.getUserDetailsByEmail(username, password);
        if (userDetailsByNick != null) {
            returnValue.setResult(true);
        } else if (userDetailsByEmail != null) {
            returnValue.setResult(true);
        }

        System.out.println("UserDetailsByNickOrEmail: " + userDetailsByNick);
        return returnValue;
    }
}
