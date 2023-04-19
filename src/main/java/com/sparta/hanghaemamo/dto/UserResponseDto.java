package com.sparta.hanghaemamo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto<T> {
    private boolean result;
    private T data;

    public static <T> MemoResponseDto Success(T data) {
        return MemoResponseDto.set(true, data);
    }

    public static <T> MemoResponseDto <T> False() {
        return MemoResponseDto.set(false, null);
    }
}
