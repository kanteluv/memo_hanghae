package com.sparta.hanghaemamo.controller;


import com.sparta.hanghaemamo.dto.UserRequestDto;
import com.sparta.hanghaemamo.dto.UserResponseDto;
import com.sparta.hanghaemamo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public ModelAndView signupPage() {
        return new ModelAndView("signup");
    }

    @PostMapping("/signup")
    public String signup(UserRequestDto userRequestDto) {
        userService.signup(userRequestDto);
        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }

    @ResponseBody
    @PostMapping("/login")
    public String login(@RequestBody UserRequestDto userRequestDto, HttpServletResponse response) {
        userService.login(userRequestDto, response);
        return "success";
    }

//    @ResponseBody
//    @PostMapping("/user/login")
//    public UserResponseDto<UserRequestDto> login(@RequestBody UserRequestDto userRequestDto, HttpServletResponse response) {
//        return userService.login(userRequestDto, response);
//    }

}