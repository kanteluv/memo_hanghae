package com.sparta.hanghaemamo.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.hanghaemamo.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class MemoCommentResponseDto {
    private Long id;

    private String username;

    private String contents;

    private String contentName;

    private Comment comments;

    public MemoCommentResponseDto(Long id, String username, String contents, String contentName, Comment comments) {
        this.id = id;
        this.username = username;
        this.contents = contents;
        this.contentName = contentName;
        this.comments = comments;
    }
}
