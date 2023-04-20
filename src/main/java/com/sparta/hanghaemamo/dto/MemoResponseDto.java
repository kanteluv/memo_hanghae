package com.sparta.hanghaemamo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "set")
public class MemoResponseDto<T> {
    private String result;
    private T data;

    public static <T> MemoResponseDto Success(T data) {
        return MemoResponseDto.set("성공하였습니다!", data);
    }

    public static <T> MemoResponseDto <T> False() {
        return MemoResponseDto.set("실패하였습니다", null);
    }
}
