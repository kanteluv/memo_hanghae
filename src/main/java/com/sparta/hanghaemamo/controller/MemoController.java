package com.sparta.hanghaemamo.controller;

import com.sparta.hanghaemamo.dto.*;
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
    @PreAuthorize("isAuthenticated()")
    public MemoResponseDto<MemoRequestDto> createMemo(@RequestBody MemoRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memoService.createMemo(requestDto, userDetails.getUser());
    }

    @PostMapping("/memos/love/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseDto createMemoLove(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memoService.createMemoLove(id, userDetails.getUser());
    }

    @GetMapping("/memos")
    @PreAuthorize("isAuthenticated()")
    public List<MemoCommentResponseDto> getMemos() {
        return memoService.getMemos();
    }


    @GetMapping("/memos/{id}")
    public MemoCommentResponseDto searchMemo(@PathVariable Long id) {
        return memoService.searchMemo(id);
    }

    @PutMapping("/memos/{id}")
    @PreAuthorize("isAuthenticated() or hasRole('ADMIN')")
    public MemoResponseDto<MemoRequestDto> updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memoService.update(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/memos/{id}")
    @PreAuthorize("isAuthenticated() or hasRole('ADMIN')")
    public ResponseDto deleteMemo(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memoService.delete(id, userDetails.getUser());
    }
}
