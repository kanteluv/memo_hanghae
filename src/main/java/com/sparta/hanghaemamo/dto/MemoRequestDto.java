package com.sparta.hanghaemamo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemoRequestDto {
//    private String username;
    private String contents;
    private String contentName;
//    private String pwd;

    public MemoRequestDto(String contents, String contentName) {
        this.contents = contents;
        this.contentName = contentName;
    }


//    public MemoRequestDto(String username, String contents, String contentName, String pwd) {
//        this.username = username;
//        this.contents = contents;
//        this.contentName = contentName;
//        this.pwd = pwd;
//    }
}