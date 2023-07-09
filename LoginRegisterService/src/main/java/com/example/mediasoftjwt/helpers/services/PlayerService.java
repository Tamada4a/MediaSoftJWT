package com.example.mediasoftjwt.helpers.services;

import com.example.mediasoftjwt.dtos.CredentialsDTO;
import com.example.mediasoftjwt.dtos.LoginDTO;
import com.example.mediasoftjwt.dtos.UserDTO;
import com.example.mediasoftjwt.helpers.Requests;
import com.example.mediasoftjwt.mysql.interfaces.UserInfoRepository;
import com.example.mediasoftjwt.mysql.tables.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;


@RequiredArgsConstructor
@Service
public class PlayerService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;


    public ResponseEntity<?> login(LoginDTO user){
        if (!userInfoRepository.existsByLogin(user.getLogin()))
            return Requests.badRequest(HttpStatus.NOT_FOUND, "Пользователя с таким логином не существует", null);

        UserInfo userInfo = userInfoRepository.findByLogin(user.getLogin()).get(0);

        if (passwordEncoder.matches(CharBuffer.wrap(user.getPassword()), userInfo.getPassword()))
            return Requests.ok(UserInfoToUserDTO(userInfo));
        return Requests.badRequest(HttpStatus.BAD_REQUEST, "Неправильный пароль", null);
    }


    public ResponseEntity<?> register(CredentialsDTO user){
        if (userInfoRepository.existsByLogin(user.getLogin()))
            return Requests.badRequest(HttpStatus.BAD_REQUEST, "Пользователь с таким логином уже существует", null);

        if (userInfoRepository.existsByEmail(user.getEmail()))
            return Requests.badRequest(HttpStatus.BAD_REQUEST, "Пользователь с такой почтой уже существует", null);

        UserInfo userInfo = CredentialsToUserInfo(user);
        userInfo.setPassword(passwordEncoder.encode(CharBuffer.wrap(user.getPassword())));

        UserInfo savedUser = userInfoRepository.save(userInfo);

        return Requests.ok(UserInfoToUserDTO(savedUser));
    }


    private UserInfo CredentialsToUserInfo(CredentialsDTO user) {
        return new UserInfo(user.getLogin(), user.getPassword(), user.getEmail());
    }


    private UserDTO UserInfoToUserDTO(UserInfo userInfo) {
        return new UserDTO(userInfo.getLogin(), userInfo.getEmail(), "");
    }
}
