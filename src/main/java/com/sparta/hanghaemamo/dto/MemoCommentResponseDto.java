package com.sparta.hanghaemamo.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.hanghaemamo.entity.Comment;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MemoCommentResponseDto {
    private final Long id;

    private final String username;

    private final String contents;

    private final String contentName;

    private final List<Comment> comments;

    public MemoCommentResponseDto(Long id, String username, String contents, String contentName, List<Comment> comments) {
        this.id = id;
        this.username = username;
        this.contents = contents;
        this.contentName = contentName;
        this.comments = comments;
    }
}
