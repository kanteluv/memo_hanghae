package com.sparta.hanghaemamo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class MemoLove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memoId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private boolean love;

    public MemoLove(Long memoId, String username, boolean love) {
        this.memoId = memoId;
        this.username = username;
        this.love = love;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
