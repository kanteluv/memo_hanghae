package com.sparta.hanghaemamo.entity;
//import com.sparta.hanghaemamo.dto.MemoRequestDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.hanghaemamo.dto.MemoRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Setter
@NoArgsConstructor
public class Memo extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String contentName;

    @OneToMany(mappedBy = "memo", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("modifiedAt DESC")
    @JsonManagedReference
    //cascade 속성은 Memo 엔티티에서 Comment 엔티티를 제거할 때 관련된 Comment 엔티티도 함께 제거됨
    //orphanRemoval 속성은 Memo 엔티티에서 Comment 엔티티가 제거되면 해당 Comment 엔티티를 DB에서도 제거함
    private List<Comment> comments = new ArrayList<>();

    public Memo(MemoRequestDto requestDto) {
        this.contents = requestDto.getContents();
        this.contentName = requestDto.getContentName();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void update(MemoRequestDto memoRequestDto) {
        this.contents = memoRequestDto.getContents();
        this.contentName = memoRequestDto.getContentName();
    }

}