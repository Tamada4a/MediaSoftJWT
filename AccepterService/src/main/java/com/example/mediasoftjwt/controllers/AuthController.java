package com.example.mediasoftjwt.controllers;

import com.example.mediasoftjwt.jwt.UserAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class AuthController {


    @Autowired
    private UserAuthProvider userAuthProvider;


    @PostMapping("/loginTest")
    public ResponseEntity<String> login(@RequestHeader("email-header") String email) {
        System.out.println("Почти из токена: " + email);

        return ResponseEntity.ok(email);
    }
}
