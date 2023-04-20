package com.sparta.hanghaemamo.service;

import com.sparta.hanghaemamo.dto.MemoRequestDto;
import com.sparta.hanghaemamo.dto.MemoResponseDto;
import com.sparta.hanghaemamo.dto.ResponseDto;
import com.sparta.hanghaemamo.entity.Memo;
import com.sparta.hanghaemamo.entity.User;
import com.sparta.hanghaemamo.entity.UserRoleEnum;
import com.sparta.hanghaemamo.repository.MemoRepository;
import com.sparta.hanghaemamo.repository.UserRepository;
import com.sparta.hanghaemamo.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;
    private final JwtUtil jwtUtil;
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String SUBJECT_KEY = "sub";

    @Transactional
    public MemoResponseDto<MemoRequestDto> createMemo(MemoRequestDto requestDto, HttpServletRequest request) {
        Memo memo = new Memo(requestDto);
        String chkToken = jwtUtil.resolveToken(request);
        Claims test = jwtUtil.getUserInfoFromToken(chkToken);
        memo.setUsername(test.getSubject());

        if (jwtUtil.validateToken(chkToken)) {
            memoRepository.save(memo);
            return MemoResponseDto.Success(memo);
        } else
            return MemoResponseDto.False();
    }

    @Transactional(readOnly = true)
    public MemoResponseDto<MemoRequestDto> getMemos() {
        List<Memo> memo = memoRepository.findAllByOrderByModifiedAtDesc();
        return MemoResponseDto.Success(memo);
    }

    @Transactional(readOnly = true)
    public MemoResponseDto<MemoRequestDto> searchMemos(Long id) {
        Memo memo = memoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("아이디가 존재하지 않아요"));
        return MemoResponseDto.Success(memo);
    }

    @Transactional
    public MemoResponseDto<MemoRequestDto> update(Long id, MemoRequestDto requestDto, HttpServletRequest request) {
        boolean chk_dto = false;
        try {
            Memo memo = memoRepository.findById(id).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 게시글입니다.")
            );
            String token = jwtUtil.resolveToken(request);

            if (token != null) {
                if (jwtUtil.validateToken(token)) {
                    Claims claims = jwtUtil.getUserInfoFromToken(token);
                    String username = claims.get(SUBJECT_KEY, String.class);
                    String role = claims.get(AUTHORIZATION_KEY, String.class);
                    if (role == UserRoleEnum.USER.name()) {
                        if (StringUtils.equals(memo.getUsername(), username)) {
                            // StringUtils.equals를 쓴 이유는 Null-safe하게 사용하기 위해서 -> Java가 잘못함
                            memo.update(requestDto);
                            return MemoResponseDto.Success(memo);
                        }
                    } else {
                        memo.update(requestDto);
                        return MemoResponseDto.Success(memo);
                    }
                }
            }
            return MemoResponseDto.False();

        } catch (NullPointerException e) {
            return MemoResponseDto.False();
        }
    }

    @Transactional
    public ResponseDto delete(Long id, HttpServletRequest request) {
        try {
            Memo memo = memoRepository.findById(id).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 게시글입니다.")
            );

            String token = jwtUtil.resolveToken(request);

            if (token != null) {
                if (jwtUtil.validateToken(token)) {
                    Claims claims = jwtUtil.getUserInfoFromToken(token);
                    String username = claims.get(SUBJECT_KEY, String.class);
                    String role = claims.get(AUTHORIZATION_KEY, String.class);
                    if (role == UserRoleEnum.USER.name()) {
                        if (StringUtils.equals(memo.getUsername(), username)) {
                            memoRepository.deleteById(id);
                            return new ResponseDto("댓글 삭제 성공했습니다!!!", HttpStatus.OK);
                        }
                    } else {
                        memoRepository.deleteById(id);
                        return new ResponseDto("댓글 삭제 성공했습니다!!!", HttpStatus.OK);
                    }
                }
            }
            return new ResponseDto("댓글 삭제 실패했습니다!!!", HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            return new ResponseDto("오류로 실패했습니다!!!", HttpStatus.BAD_REQUEST);
        }
    }
}