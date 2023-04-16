package com.sparta.hanghaemamo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoRequestDto {
    private String username;
    private String contents;
    private String contentName;
    private String pwd;
}
