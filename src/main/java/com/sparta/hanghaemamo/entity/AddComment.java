package com.sparta.hanghaemamo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sparta.hanghaemamo.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
//@Data
@Entity
@NoArgsConstructor
public class AddComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "COMMENT_ID", nullable = false)
    @JsonBackReference
    private Comment comment;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String username;

    public AddComment(CommentRequestDto commentRequestDto) {
        this.contents = commentRequestDto.getContents();
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void update(CommentRequestDto commentRequestDto){
        this.contents = commentRequestDto.getContents();
    }
}
