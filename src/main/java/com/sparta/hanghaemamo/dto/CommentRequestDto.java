package com.sparta.hanghaemamo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequestDto {
    private Long id;
    private String contents;

    public CommentRequestDto(Long id, String contents) {
        this.id = id;
        this.contents = contents;
    }
}
