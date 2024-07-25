package com.lucas.mastermind.util;

import com.lucas.mastermind.entity.User;
import com.lucas.mastermind.repository.UserRepository;
import com.lucas.mastermind.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitialSetup {

    @Autowired
    UserService userService;
    PasswordEncoder passwordEncoder;


//    @EventListener
//    @Transactional
//    public void onApplicationEvent(ApplicationReadyEvent event) {
//        User user = new User("user18", "user18@example.com", "user18");
////        String encodedPassword = passwordEncoder.encode(user.getPassword());
////        user.setPassword(encodedPassword);
//        userService.saveUser(user);
//    }
}