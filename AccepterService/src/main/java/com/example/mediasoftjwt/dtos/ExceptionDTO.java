package com.example.mediasoftjwt.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ExceptionDTO {
    private String description;

    private String exceptionMessage;
}
