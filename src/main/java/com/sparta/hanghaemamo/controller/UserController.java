package com.sparta.hanghaemamo.controller;

import com.sparta.hanghaemamo.dto.UserRequestDto;
import com.sparta.hanghaemamo.dto.UserResponseDto;
import com.sparta.hanghaemamo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public UserResponseDto signup(@RequestBody UserRequestDto requestDto) {
        return userService.signup(requestDto);
    }

    @PostMapping("/login")
    public UserResponseDto login(@RequestBody UserRequestDto requestDto, HttpServletResponse response) {
        return userService.login(requestDto, response);
    }
}