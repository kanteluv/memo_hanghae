package com.sparta.hanghaemamo.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.hanghaemamo.entity.Comment;
import com.sparta.hanghaemamo.entity.Memo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MemoCommentResponseDto {
    private final Long id;

    private final String username;

    private final String contents;

    private final String contentName;

    private final Long loveCnt;

    private final List<WithoutMemoResponseDto> comments;



    public MemoCommentResponseDto(Memo memo, List<WithoutMemoResponseDto> comments, Long loveCnt) {
        this.id = memo.getId();
        this.username = memo.getUsername();
        this.contents = memo.getContents();
        this.contentName = memo.getContentName();
        this.loveCnt = loveCnt;
        this.comments = comments;
    }
}
