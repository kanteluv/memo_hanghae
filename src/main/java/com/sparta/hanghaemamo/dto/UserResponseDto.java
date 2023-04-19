package com.sparta.hanghaemamo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "set")
public class UserResponseDto<T> {
    private boolean result;
    private T data;

    public static <T> UserResponseDto Success(T data) {
        return UserResponseDto.set(true, data);
    }

    public static <T> UserResponseDto <T> False() {
        return UserResponseDto.set(false, null);
    }
}
