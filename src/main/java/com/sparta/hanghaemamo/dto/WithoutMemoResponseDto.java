package com.sparta.hanghaemamo.dto;

import com.sparta.hanghaemamo.entity.Comment;
import com.sparta.hanghaemamo.entity.Memo;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
public class WithoutMemoResponseDto {
    private Long id;
    private String contents;
    private String username;
    private Long loveCnt;

    public WithoutMemoResponseDto(Comment comment) {
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.username = comment.getUsername();
        this.loveCnt = comment.getLoveCnt();
    }
}
