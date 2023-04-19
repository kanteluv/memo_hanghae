package com.sparta.hanghaemamo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private String msg;
    private HttpStatus code;
}
