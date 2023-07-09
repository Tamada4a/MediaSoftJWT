package com.example.mediasoftjwt.controllers;

import com.example.mediasoftjwt.dtos.CredentialsDTO;
import com.example.mediasoftjwt.dtos.LoginDTO;
import com.example.mediasoftjwt.dtos.ResponseDTO;
import com.example.mediasoftjwt.dtos.UserDTO;
import com.example.mediasoftjwt.helpers.Requests;
import com.example.mediasoftjwt.jwt.UserAuthProvider;
import com.example.mediasoftjwt.helpers.services.PlayerService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private UserAuthProvider userAuthProvider;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) throws JOSEException {
        ResponseEntity<?> response = playerService.login(loginDTO);

        if (response.getStatusCode() != HttpStatus.OK){
            return response;
        }

        UserDTO userDTO = (UserDTO) ((ResponseDTO) response.getBody()).getResult();

        userDTO.setToken(userAuthProvider.createToken(userDTO.getLogin(), userDTO.getEmail()));

        return Requests.ok(userDTO);
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CredentialsDTO registrationDTO) throws JOSEException {
        ResponseEntity<?> response = playerService.register(registrationDTO);

        if (response.getStatusCode() != HttpStatus.OK){
            return response;
        }

        UserDTO userDTO = (UserDTO) ((ResponseDTO) response.getBody()).getResult();

        return ResponseEntity.created(URI.create("/" + userDTO.getLogin())).body(userDTO);
    }
}
