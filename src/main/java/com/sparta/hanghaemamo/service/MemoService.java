package com.sparta.hanghaemamo.service;

import com.sparta.hanghaemamo.dto.MemoRequestDto;
import com.sparta.hanghaemamo.dto.MemoResponseDto;
import com.sparta.hanghaemamo.entity.Memo;
import com.sparta.hanghaemamo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;

    @Transactional
    public MemoResponseDto<MemoRequestDto> createMemo(MemoRequestDto requestDto) {
        Memo memo = new Memo(requestDto);
        memoRepository.save(memo);
        return MemoResponseDto.Success(memo);
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
    public MemoResponseDto<MemoRequestDto> delete(Long id, String pwd) {
        Memo memo = memoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("아이디가 존재하지 않아요"));

        if (memo.getPwd().equals(pwd)) {
            memoRepository.deleteById(id);
            return MemoResponseDto.Success(null);
        } else {
            return MemoResponseDto.False();
        }
    }

}
