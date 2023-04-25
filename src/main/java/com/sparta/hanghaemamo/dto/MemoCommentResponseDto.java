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
    private final Long id;

    private final String username;

    private final String contents;

    private final String contentName;

    private final List<Comment> comments;

    public MemoCommentResponseDto(Memo memo, List<Comment> comments) {
        this.id = memo.getId();
        this.username = memo.getUsername();
        this.contents = memo.getContents();
        this.contentName = memo.getContentName();
        this.comments = comments;
    }
}
