package com.sparta.hanghaemamo.controller;

import com.sparta.hanghaemamo.dto.MemoRequestDto;
import com.sparta.hanghaemamo.dto.MemoResponseDto;
import com.sparta.hanghaemamo.dto.ResponseDto;
import com.sparta.hanghaemamo.entity.Memo;
import com.sparta.hanghaemamo.entity.UserRoleEnum;
import com.sparta.hanghaemamo.security.UserDetailsImpl;
import com.sparta.hanghaemamo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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

    //HttpServletRequest 을 서비스단에서가 아니라 컨트롤러단에서만 할수있을까?
    //클라이언트 요청이므로 컨트롤단에서만 처리하고싶다
//    @Secured(UserRoleEnum.Authority.ADMIN)
    @PostMapping("/memos")
    public MemoResponseDto<MemoRequestDto> createMemo(@RequestBody MemoRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memoService.createMemo(requestDto, userDetails.getUser());
    }

    @GetMapping("/memos")
    public MemoResponseDto<Memo> getMemos() {
        return memoService.getMemos();
    }

    @GetMapping("/memos/{id}")
    public MemoResponseDto<MemoRequestDto> searchMemos(@PathVariable Long id) {
        return memoService.searchMemos(id);
    }

    @PutMapping("/memos/{id}")
    @PreAuthorize("isAuthenticated() and (hasRole('ADMIN') or #userDetails.username == authentication.principal)")
    public MemoResponseDto<MemoRequestDto> updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memoService.update(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/memos/{id}")
    public ResponseDto deleteMemo(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memoService.delete(id, userDetails.getUser());
    }
}
