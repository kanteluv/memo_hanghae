package com.sparta.hanghaemamo.service;

import com.sparta.hanghaemamo.dto.MemoRequestDto;
import com.sparta.hanghaemamo.dto.MemoResponseDto;
import com.sparta.hanghaemamo.entity.Memo;
import com.sparta.hanghaemamo.entity.User;
import com.sparta.hanghaemamo.entity.UserRoleEnum;
import com.sparta.hanghaemamo.repository.MemoRepository;
import com.sparta.hanghaemamo.repository.UserRepository;
import com.sparta.hanghaemamo.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    public static final String AUTHORIZATION_KEY = "auth";


    //서비스 -> 서버로 가는 dto
    //클라이언트 로 응답하는 dto 분리하면 좋을거같다
    //제너릭 <?> 이면 컴파일시에 맞는 타입 체크를 못하기때문에 의미가 없다
    //엔티티가 리턴되고 있는데 open session in view 의 관점에서 생각해보라
    //entity -> dto 로 검색해보자
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
        try {
            Memo memo = memoRepository.findById(id).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 게시글입니다.")
            );

            String token = jwtUtil.resolveToken(request);
            Map<String, Object> claimMap = jwtUtil.createClaims(memo);

            if (token != null) {
                if (jwtUtil.validateToken(token)) {
                    claims = jwtUtil.getUserInfoFromToken(token);
                    if (StringUtils.equals(memo.getUsername(), id)) {
                        // StringUtils.equals를 쓴 이유는 Null-safe하게 사용하기 위해서 -> Java가 잘못함

                        memo.update(requestDto);
                        return MemoResponseDto.Success(memo);
                    }
                    User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                            () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
                    );

                    // 사용자 권한 가져와서 ADMIN 이면 전체 조회, USER 면 본인이 추가한 부분 조회
                    UserRoleEnum userRoleEnum = user.getRole();
                    System.out.println("role = " + userRoleEnum);
                }
            }

            return MemoResponseDto.False();

        } catch (NullPointerException e) {
            return MemoResponseDto.False();
        }
    }

    @Transactional
    public MemoResponseDto<MemoRequestDto> delete(Long id, HttpServletRequest request) {
        try {
            Memo memo = memoRepository.findById(id).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 게시글입니다.")
            );

            String token = jwtUtil.resolveToken(request);
            Claims claims;

            if (token != null) {
                if (jwtUtil.validateToken(token)) {
                    claims = jwtUtil.getUserInfoFromToken(token);
                    if (memo.getUsername().equals(claims.getSubject())) {
                        memoRepository.deleteById(id);
                        return MemoResponseDto.Success(memo);
                    }
                }
            }

            return MemoResponseDto.False();

        } catch (NullPointerException e) {
            return MemoResponseDto.False();
        }
    }
}
