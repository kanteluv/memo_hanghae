package com.sparta.hanghaemamo.service;


import com.sparta.hanghaemamo.dto.MemoResponseDto;
import com.sparta.hanghaemamo.dto.UserRequestDto;
import com.sparta.hanghaemamo.dto.UserResponseDto;
import com.sparta.hanghaemamo.entity.User;

//import com.sparta.hanghaemamo.jwt.JwtUtil;
import com.sparta.hanghaemamo.repository.UserRepository;
import com.sparta.hanghaemamo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserResponseDto signup(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        String password = userRequestDto.getPassword();

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        User user = new User(userRequestDto);
        userRepository.save(user);

        UserResponseDto userResponseDto = new UserResponseDto("Success", HttpStatus.OK);

        return userResponseDto;
    }


    @Transactional(readOnly = true)
    public UserResponseDto login(UserRequestDto requestDto, HttpServletResponse response) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
        // 비밀번호 확인
        if(!user.getPassword().equals(password)){
            throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        //JWT
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername()));
        UserResponseDto userResponseDto = new UserResponseDto("Success", HttpStatus.OK);

        return userResponseDto;

    }
}