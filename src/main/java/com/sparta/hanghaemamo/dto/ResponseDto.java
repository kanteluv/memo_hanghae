package com.sparta.hanghaemamo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ResponseDto {
    private String msg;
    private HttpStatus code;
}
