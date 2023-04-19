package com.sparta.hanghaemamo.entity;
//import com.sparta.hanghaemamo.dto.MemoRequestDto;
import com.sparta.hanghaemamo.dto.MemoRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Memo extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String contentName;

    public Memo(MemoRequestDto requestDto) {
        this.contents = requestDto.getContents();
        this.contentName = requestDto.getContentName();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void update(MemoRequestDto memoRequestDto) {
        this.contents = memoRequestDto.getContents();
        this.contentName = memoRequestDto.getContentName();
    }


}