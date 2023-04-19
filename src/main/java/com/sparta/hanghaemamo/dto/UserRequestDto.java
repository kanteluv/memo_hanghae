package com.sparta.hanghaemamo.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    private String username;
    private String password;
}
