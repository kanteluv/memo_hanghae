package com.sparta.hanghaemamo.controller;

import com.sparta.hanghaemamo.dto.MemoRequestDto;
import com.sparta.hanghaemamo.dto.MemoResponseDto;
import com.sparta.hanghaemamo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;
    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    @PostMapping("/api/memos")
    public MemoResponseDto<MemoRequestDto> createMemo(@RequestBody MemoRequestDto requestDto) {
        return memoService.createMemo(requestDto);
    }

    @GetMapping("/api/memos")
    public MemoResponseDto<MemoRequestDto> getMemos() {
        return memoService.getMemos();
    }


    @GetMapping("/api/memos/{id}")
    public MemoResponseDto<MemoRequestDto> searchMemos(@PathVariable Long id) {
        return memoService.searchMemos(id);
    }

    @PutMapping("/api/memos/{id}")
    public MemoResponseDto<MemoRequestDto> updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        return memoService.update(id, requestDto);
    }

    @DeleteMapping("/api/memos/{id}")
    public MemoResponseDto<MemoRequestDto> deleteMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        return memoService.delete(id, requestDto);
    }

}
