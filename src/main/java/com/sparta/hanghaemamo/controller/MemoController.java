package com.sparta.hanghaemamo.controller;

import com.sparta.hanghaemamo.dto.MemoRequestDto;
import com.sparta.hanghaemamo.dto.MemoResponseDto;
import com.sparta.hanghaemamo.entity.Memo;
import com.sparta.hanghaemamo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemoController {

    private final MemoService memoService;
    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    @PostMapping("/memos")
    public MemoResponseDto<MemoRequestDto> createMemo(@RequestBody MemoRequestDto requestDto, HttpServletRequest request) {
        return memoService.createMemo(requestDto, request);
    }

    @GetMapping("/memos")
    public MemoResponseDto<MemoRequestDto> getMemos() {
        return memoService.getMemos();
    }


    @GetMapping("/memos/{id}")
    public MemoResponseDto<MemoRequestDto> searchMemos(@PathVariable Long id) {
        return memoService.searchMemos(id);
    }

    @PutMapping("/memos/{id}")
    public MemoResponseDto<MemoRequestDto> updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        return memoService.update(id, requestDto);
    }

    @DeleteMapping("/memos/{id}")
    public MemoResponseDto<MemoRequestDto> deleteMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        return memoService.delete(id, requestDto);
    }

}
