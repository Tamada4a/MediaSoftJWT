package com.example.mediasoftjwt.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserDTO {
    private String login;

    private String email;

    private String token;
}
