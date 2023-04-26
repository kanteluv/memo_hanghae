package com.sparta.hanghaemamo.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class CommentLove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long commentId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private boolean love;

    public CommentLove(Long commentId, String username, boolean love) {
        this.commentId = commentId;
        this.username = username;
        this.love = love;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
