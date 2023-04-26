package com.sparta.hanghaemamo.controller;

import com.sparta.hanghaemamo.dto.*;
import com.sparta.hanghaemamo.entity.Comment;
import com.sparta.hanghaemamo.security.UserDetailsImpl;
import com.sparta.hanghaemamo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{memoId}/comments")
    @PreAuthorize("isAuthenticated()")
    public CommentResponseDto<Comment> createComment(@PathVariable Long memoId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(memoId, requestDto, userDetails.getUser());
    }

    @PostMapping("/comments/love/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseDto createCommentLove(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createCommentLove(commentId, userDetails.getUser());
    }

    @PutMapping("/comments/{id}")
    @PreAuthorize("isAuthenticated()")
    public CommentResponseDto<CommentRequestDto> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/comments/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseDto deleteMemo(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.delete(id, userDetails.getUser());
    }

}
