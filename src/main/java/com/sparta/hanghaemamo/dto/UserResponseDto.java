package com.sparta.hanghaemamo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private String msg;
    private HttpStatus data;

}
