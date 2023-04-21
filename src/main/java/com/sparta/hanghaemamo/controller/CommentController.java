package com.sparta.hanghaemamo.controller;

import com.sparta.hanghaemamo.dto.*;
import com.sparta.hanghaemamo.entity.Comment;
import com.sparta.hanghaemamo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{memoId}/comments")
    public CommentResponseDto<Comment> createComment(@PathVariable Long memoId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.createComment(memoId, requestDto, request);
    }

    @PutMapping("/comments/{id}")
    public CommentResponseDto<CommentRequestDto> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.updateComment(id, requestDto, request);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseDto deleteMemo(@PathVariable Long id, HttpServletRequest request) {
        return commentService.delete(id, request);
    }
}
