package com.sparta.hanghaemamo.entity;


import com.sparta.hanghaemamo.dto.UserRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity(name="USERS")
@Table(name="USERS")
@NoArgsConstructor
public class User extends Timestamped {
    @Id
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    public User(UserRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
    }

}
