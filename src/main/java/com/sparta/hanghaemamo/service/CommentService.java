package com.sparta.hanghaemamo.service;

import com.sparta.hanghaemamo.dto.*;
import com.sparta.hanghaemamo.entity.Comment;
import com.sparta.hanghaemamo.entity.Memo;
import com.sparta.hanghaemamo.entity.UserRoleEnum;
import com.sparta.hanghaemamo.repository.CommentRepository;
import com.sparta.hanghaemamo.repository.MemoRepository;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemoRepository memoRepository;
    private final JwtUtil jwtUtil;
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String SUBJECT_KEY = "sub";

    @Transactional
    public CommentResponseDto<Comment> createComment(Long memoId, CommentRequestDto requestDto, HttpServletRequest request) {
        Memo memo = memoRepository.findById(memoId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글 입니다")
        );
        Comment comment = new Comment(requestDto);
        comment.setMemo(memo);
        String chkToken = jwtUtil.resolveToken(request);
        Claims test = jwtUtil.getUserInfoFromToken(chkToken);
        comment.setUsername(test.getSubject());

        if (jwtUtil.validateToken(chkToken)) {
            commentRepository.save(comment);
            return CommentResponseDto.Success(comment);
        } else
            return CommentResponseDto.False();
    }

    @Transactional
    public CommentResponseDto<CommentRequestDto> updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        try {
            Comment comment = commentRepository.findById(id).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 댓글입니다.")
            );
            String token = jwtUtil.resolveToken(request);

            if (token != null) {
                if (jwtUtil.validateToken(token)) {
                    Claims claims = jwtUtil.getUserInfoFromToken(token);
                    String username = claims.get(SUBJECT_KEY, String.class);
                    String role = claims.get(AUTHORIZATION_KEY, String.class);
                    if (role == UserRoleEnum.USER.name()) {
                        if (StringUtils.equals(comment.getUsername(), username)) {
                            // StringUtils.equals를 쓴 이유는 Null-safe하게 사용하기 위해서 -> Java가 잘못함
                            comment.update(requestDto);
                            return CommentResponseDto.Success(comment);
                        }
                    } else {
                        comment.update(requestDto);
                        return CommentResponseDto.Success(comment);
                    }
                }
            }
            return CommentResponseDto.False();

        } catch (NullPointerException e) {
            return CommentResponseDto.False();
        }
    }

    @Transactional
    public ResponseDto delete(Long id, HttpServletRequest request) {
        try {
            Comment comment = commentRepository.findById(id).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 댓글입니다.")
            );

            String token = jwtUtil.resolveToken(request);

            if (token != null) {
                if (jwtUtil.validateToken(token)) {
                    Claims claims = jwtUtil.getUserInfoFromToken(token);
                    String username = claims.get(SUBJECT_KEY, String.class);
                    String role = claims.get(AUTHORIZATION_KEY, String.class);
                    if (StringUtils.equals(role, UserRoleEnum.USER.name())) {
                        if (StringUtils.equals(comment.getUsername(), username)) {
                            commentRepository.deleteById(id);
                            return new ResponseDto("댓글 삭제 성공했습니다!!!", HttpStatus.OK);
                        }
                    } else {
                        commentRepository.deleteById(id);
                        return new ResponseDto("댓글 삭제 성공했습니다!!!", HttpStatus.OK);
                    }
                }
            }
            return new ResponseDto("댓글 삭제 실패했습니다!!!", HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            return new ResponseDto("오류로 실패했습니다!!!", HttpStatus.BAD_REQUEST);
        }
    }

    public List<Comment> getComments(Long memoId){
        return commentRepository.findAllByMemoId(memoId);
    }

    public List<Comment> getAllComments(){
        return commentRepository.findAll();
    }
}
