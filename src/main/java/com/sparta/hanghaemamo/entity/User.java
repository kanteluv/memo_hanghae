package com.sparta.hanghaemamo.entity;


import com.sparta.hanghaemamo.dto.UserRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public User(UserRequestDto requestDto, UserRoleEnum role) {
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        this.role = role;
    }

}
