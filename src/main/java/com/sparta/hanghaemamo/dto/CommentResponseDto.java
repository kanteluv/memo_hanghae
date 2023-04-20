package com.sparta.hanghaemamo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "set")
public class CommentResponseDto<T> {
    private boolean result;
    private T data;

    public static <T> CommentResponseDto Success(T data) {
        return CommentResponseDto.set(true, data);
    }

    public static <T> CommentResponseDto <T> False() {
        return CommentResponseDto.set(false, null);
    }
}
