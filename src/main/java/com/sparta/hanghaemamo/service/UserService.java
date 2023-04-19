package com.sparta.hanghaemamo.service;

import com.sparta.hanghaemamo.dto.UserRequestDto;
import com.sparta.hanghaemamo.dto.ResponseDto;
import com.sparta.hanghaemamo.entity.User;
import com.sparta.hanghaemamo.repository.UserRepository;
import com.sparta.hanghaemamo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final  UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public ResponseDto signup(UserRequestDto requestDto) {
        Optional<User> found = userRepository.findById(requestDto.getUsername());

        if (found.isPresent()) {
            return new ResponseDto("실패", HttpStatus.BAD_REQUEST);
        }

        User user = new User(requestDto);
        userRepository.save(user);


        ResponseDto responseDto = new ResponseDto("Success", HttpStatus.OK);

        return responseDto;
    }

    public ResponseDto login(UserRequestDto requestDto, HttpServletResponse response) {
        try {
            User user = userRepository.findById(requestDto.getUsername()).orElseThrow(
                    () -> new IllegalArgumentException("없는 ID 입니다.")
            );


            if (requestDto.getPassword().equals(user.getPassword())) {
                response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername()));
                return new ResponseDto("성공", HttpStatus.OK);
            }

            return new ResponseDto("비밀번호가 틀렸습니다.", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
