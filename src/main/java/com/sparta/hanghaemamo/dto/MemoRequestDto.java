package com.sparta.hanghaemamo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemoRequestDto {
    private String contents;
    private String contentName;

    public MemoRequestDto(String contents, String contentName) {
        this.contents = contents;
        this.contentName = contentName;
    }

}