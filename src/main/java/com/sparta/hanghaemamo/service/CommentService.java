package com.sparta.hanghaemamo.service;

import com.sparta.hanghaemamo.dto.*;
import com.sparta.hanghaemamo.entity.*;
import com.sparta.hanghaemamo.repository.CommentLoveRepository;
import com.sparta.hanghaemamo.repository.CommentRepository;
import com.sparta.hanghaemamo.repository.MemoRepository;
import com.sparta.hanghaemamo.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemoRepository memoRepository;
    private final CommentLoveRepository commentLoveRepository;
    private final JwtUtil jwtUtil;
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String SUBJECT_KEY = "sub";

    @Transactional
    public CommentResponseDto<Comment> createComment(Long memoId, CommentRequestDto requestDto, User user) {
        Memo memo = memoRepository.findById(memoId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글 입니다")
        );
        Comment comment = new Comment(requestDto);
        comment.setMemo(memo);
//        String chkToken = jwtUtil.resolveToken(request);
//        Claims test = jwtUtil.getUserInfoFromToken(chkToken);
        Long loveCnt = 0L;
        comment.setUsername(user.getUsername());
        comment.setLoveCnt(loveCnt);
        commentRepository.save(comment);
        WithoutMemoResponseDto withoutMemoResponseDto = new WithoutMemoResponseDto(comment);

//        if (jwtUtil.validateToken(chkToken)) {
//            commentRepository.save(comment);
//            WithoutMemoResponseDto withoutMemoResponseDto = new WithoutMemoResponseDto(comment);
//            return CommentResponseDto.Success(withoutMemoResponseDto);
//        } else
//            return CommentResponseDto.False();

        return CommentResponseDto.Success(withoutMemoResponseDto);
    }

    @Transactional
    public ResponseDto createCommentLove(Long id, User user) {
        try {
            Comment comment = commentRepository.findById(id).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 댓글입니다.")
            );
            String loveId = user.getUsername();
            Long chkCnt = commentLoveRepository.findCommentIdAndUserName(id, loveId);

            if(chkCnt == 0) {
                CommentLove commentLove = new CommentLove(id, loveId, true);
                commentLoveRepository.save(commentLove);
                return new ResponseDto("좋댓구알 감사합니다", HttpStatus.OK);
            }
            else {
                CommentLove origincommentLove = commentLoveRepository.findByCommentIdAndUsername(id, loveId);
                if(commentLoveRepository.getLoveCheck(id, loveId)) {
                    CommentLove commentLove = new CommentLove(id, loveId, false);
                    commentLoveRepository.delete(origincommentLove);
                    commentLoveRepository.save(commentLove);
                    return new ResponseDto("취소 왜하는거죠", HttpStatus.OK);
                }
                else {
                    CommentLove commentLove = new CommentLove(id, loveId, false);
                    commentLoveRepository.delete(origincommentLove);
                    commentLoveRepository.save(commentLove);
                    return new ResponseDto("좋댓구알 감사합니다", HttpStatus.OK);
                }
            }
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    @Transactional
    public CommentResponseDto<CommentRequestDto> updateComment(Long id, CommentRequestDto requestDto, User user) {
        try {
            Comment comment = commentRepository.findById(id).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 댓글입니다.")
            );
//            String token = jwtUtil.resolveToken(request);

//            if (token != null) {
//                if (jwtUtil.validateToken(token)) {
//                    Claims claims = jwtUtil.getUserInfoFromToken(token);
//                    String username = claims.get(SUBJECT_KEY, String.class);
//                    String role = claims.get(AUTHORIZATION_KEY, String.class);
//                    if (role == UserRoleEnum.USER.name()) {
//                        if (StringUtils.equals(comment.getUsername(), username)) {
//                            // StringUtils.equals를 쓴 이유는 Null-safe하게 사용하기 위해서 -> Java가 잘못함
//                            comment.update(requestDto);
//                            return CommentResponseDto.Success(comment);
//                        }
//                    } else {
//                        comment.update(requestDto);
//                        return CommentResponseDto.Success(comment);
//                    }
//                }
//            }
//            return CommentResponseDto.False();
            UserRoleEnum userRoleEnum = user.getRole();

            switch (userRoleEnum) {
                case USER:
                    if (StringUtils.equals(comment.getUsername(), user.getUsername())) {
                        comment.update(requestDto);
                        return CommentResponseDto.Success(comment);
                    }
                    break;
                case ADMIN:
                    comment.update(requestDto);
                    return CommentResponseDto.Success(comment);
            }

            return CommentResponseDto.False();


        } catch (NullPointerException e) {
            return CommentResponseDto.False();
        }
    }

    @Transactional
    public ResponseDto delete(Long id, User user) {
        try {
            Comment comment = commentRepository.findById(id).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 댓글입니다.")
            );

//            String token = jwtUtil.resolveToken(request);
//
//            if (token != null) {
//                if (jwtUtil.validateToken(token)) {
//                    Claims claims = jwtUtil.getUserInfoFromToken(token);
//                    String username = claims.get(SUBJECT_KEY, String.class);
//                    String role = claims.get(AUTHORIZATION_KEY, String.class);
//                    if (StringUtils.equals(role, UserRoleEnum.USER.name())) {
//                        if (StringUtils.equals(comment.getUsername(), username)) {
//                            commentRepository.deleteById(id);
//                            return new ResponseDto("댓글 삭제 성공했습니다!!!", HttpStatus.OK);
//                        }
//                    } else {
//                        commentRepository.deleteById(id);
//                        return new ResponseDto("댓글 삭제 성공했습니다!!!", HttpStatus.OK);
//                    }
//                }
//            }
//            return new ResponseDto("댓글 삭제 실패했습니다!!!", HttpStatus.BAD_REQUEST);
            UserRoleEnum userRoleEnum = user.getRole();

            switch (userRoleEnum) {
                case USER:
                    if (StringUtils.equals(comment.getUsername(), user.getUsername())) {
                        memoRepository.deleteById(id);
                        return new ResponseDto("게시글 삭제 성공했습니다!!!", HttpStatus.OK);
                    }
                    break;
                case ADMIN:
                    memoRepository.deleteById(id);
                    return new ResponseDto("게시글 삭제 성공했습니다!!!", HttpStatus.OK);
            }

            return new ResponseDto("게시글 삭제 실패했습니다ㅠㅠㅠ", HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            return new ResponseDto("오류로 실패했습니다!!!", HttpStatus.BAD_REQUEST);
        }
    }

    public List<WithoutMemoResponseDto> getComments(Long memoId){
        List<Comment> comments = commentRepository.findAllByMemoId(memoId);
        List<WithoutMemoResponseDto> commentResponse = new ArrayList<>();

        for(Comment comment: comments){
            Long loveCnt = commentLoveRepository.getLoveCnt(comment.getId());
            comment.setLoveCnt(loveCnt);
            WithoutMemoResponseDto withoutMemoResponseDto = new WithoutMemoResponseDto(comment);
            commentResponse.add(withoutMemoResponseDto);

        }
        return commentResponse;
    }

    public List<Comment> getAllComments(){
        return commentRepository.findAll();
    }
}
