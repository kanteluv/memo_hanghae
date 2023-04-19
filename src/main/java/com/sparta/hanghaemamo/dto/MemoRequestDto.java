package com.sparta.hanghaemamo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemoRequestDto {
    private String contents;
    private String contentName;
}