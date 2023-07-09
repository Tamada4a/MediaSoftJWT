package com.example.mediasoftjwt.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.AuthenticationException;


@Data
@AllArgsConstructor
public class ExceptionDTO {
    private String description;

    private String exceptionMessage;
}
