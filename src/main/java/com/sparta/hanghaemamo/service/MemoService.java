package com.sparta.hanghaemamo.service;

import com.sparta.hanghaemamo.dto.MemoRequestDto;
import com.sparta.hanghaemamo.dto.MemoResponseDto;
import com.sparta.hanghaemamo.entity.Memo;
import com.sparta.hanghaemamo.repository.MemoRepository;
import com.sparta.hanghaemamo.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public MemoResponseDto<MemoRequestDto> createMemo(MemoRequestDto requestDto, HttpServletRequest request) {
        Memo memo = new Memo(requestDto);
        String chkToken = jwtUtil.resolveToken(request);
        Claims test = jwtUtil.getUserInfoFromToken(chkToken);
        memo.setUsername(test.getSubject());

        if(jwtUtil.validateToken(chkToken)) {
            memoRepository.save(memo);
            return MemoResponseDto.Success(memo);
        }
        else
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
    public MemoResponseDto<MemoRequestDto> update(Long id, MemoRequestDto requestDto) {
        Memo memo = memoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("아이디가 존재하지 않아요"));

        if (memo.getPwd().equals(requestDto.getPwd())) {
            memo.update(requestDto);
            return MemoResponseDto.Success(memo);
        } else {
            return MemoResponseDto.False();
        }
    }

    @Transactional
    public MemoResponseDto<MemoRequestDto> delete(Long id, MemoRequestDto requestDto) {
        Memo memo = memoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("아이디가 존재하지 않아요"));

        if (memo.getPwd().equals(requestDto.getPwd())) {
            memoRepository.deleteById(id);
            return MemoResponseDto.Success(null);
        } else {
            return MemoResponseDto.False();
        }
    }

}
