package com.sparta.hanghaemamo.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class UserRequestDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
