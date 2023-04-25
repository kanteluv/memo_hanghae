package com.sparta.hanghaemamo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.hanghaemamo.dto.CommentRequestDto;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
//@Data
@Entity
@NoArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMO_ID", nullable = false)
    private Memo memo;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String username;

    public Comment(CommentRequestDto commentRequestDto) {
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
