package com.sparta.hanghaemamo.service;

import com.sparta.hanghaemamo.dto.*;
import com.sparta.hanghaemamo.entity.*;
import com.sparta.hanghaemamo.repository.MemoLoveRepository;
import com.sparta.hanghaemamo.repository.MemoRepository;
import com.sparta.hanghaemamo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;
    private final MemoLoveRepository memoLoveRepository;
    private final JwtUtil jwtUtil;
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String SUBJECT_KEY = "sub";
    private final CommentService commentService;


    //서비스 -> 서버로 가는 dto
    //클라이언트 로 응답하는 dto 분리하면 좋을거같다
    //제너릭 <?> 이면 컴파일시에 맞는 타입 체크를 못하기때문에 의미가 없다
    //엔티티가 리턴되고 있는데 open session in view 의 관점에서 생각해보라
    //entity -> dto 로 검색해보자
    // User Entity를 가져올 수 밖에 읎습니다...
    @Transactional
    public MemoResponseDto<MemoRequestDto> createMemo(MemoRequestDto requestDto, User user) {
        System.out.println("ProductService.createProduct");
        System.out.println("user.getUsername() = " + user.getUsername());

        Memo memo = new Memo(requestDto);
        memo.setUsername(user.getUsername());
        Long loveCnt = 0L;
        memo.setLoveCnt(loveCnt);
        memoRepository.saveAndFlush(memo);

        return MemoResponseDto.Success(memo);
    }

    @Transactional
    public ResponseDto createMemoLove(Long id, User user) {
        try {
            Memo memo = memoRepository.findById(id).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 게시글입니다."
            ));
            String loveId = user.getUsername();
            Long chkCnt = memoLoveRepository.findMemoIdAndUserName(id, loveId);

            if(chkCnt == 0) {
                MemoLove memoLove = new MemoLove(id, loveId, true);
                memoLoveRepository.save(memoLove);
                return new ResponseDto("좋댓구알 감사합니다", HttpStatus.OK);
            }
            else {
                MemoLove originMemoLove = memoLoveRepository.findByMemoIdAndUsername(id, loveId);
                if(memoLoveRepository.getLoveCheck(id, loveId)) {
                    MemoLove memoLove = new MemoLove(id, loveId, false);
                    memoLoveRepository.delete(originMemoLove);
                    memoLoveRepository.save(memoLove);
                    return new ResponseDto("취소 왜하는거죠", HttpStatus.OK);
                }
                else {
                    MemoLove memoLove = new MemoLove(id, loveId, true);
                    memoLoveRepository.delete(originMemoLove);
                    memoLoveRepository.save(memoLove);
                    return new ResponseDto("좋댓구알 감사합니다", HttpStatus.OK);
                }
            }
//                if(memoLoveRepository.getLoveCheck(id, loveId)) {
//                        MemoLove memoLove = new MemoLove(id, loveId, false);
//                        memoLoveRepository.save(memoLove);
//                        return new ResponseDto("취소 왜하는거죠", HttpStatus.OK);
//                }
//                else {
//                    MemoLove memoLove = new MemoLove(id, loveId, true);
//                    memoLoveRepository.save(memoLove);
//                    return new ResponseDto("좋댓구알 감사합니다", HttpStatus.OK);
//                }
//            }
        }
        catch (Exception ex) {
            throw ex;
        }
    }



    @Transactional(readOnly = true)
    public List<MemoCommentResponseDto> getMemos() {
        List<MemoCommentResponseDto> commentsAndMemo = new ArrayList<>();

        List<Memo> memoList = memoRepository.allMemoList();
        for(Memo memo : memoList){
            List<WithoutMemoResponseDto> comments = commentService.getComments(memo.getId());
            Long loveCnt = memoLoveRepository.getLoveCnt(memo.getId());
            MemoCommentResponseDto memoCommentResponseDto = new MemoCommentResponseDto(memo, comments, loveCnt);
            commentsAndMemo.add(memoCommentResponseDto);
        }
        return commentsAndMemo;
    }


    @Transactional(readOnly = true)
    public MemoCommentResponseDto searchMemo(Long id) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new NullPointerException("존재하지 않는 게시글입니다.")
        );
        List<WithoutMemoResponseDto> comments = commentService.getComments(id);
        Long loveCnt = memoLoveRepository.getLoveCnt(id);
        return new MemoCommentResponseDto(memo, comments, loveCnt);
    }


    @Transactional
    public MemoResponseDto<MemoRequestDto> update(Long id, MemoRequestDto requestDto, User user) {
        try {
            Memo memo = memoRepository.findById(id).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 게시글입니다.")
            );

            UserRoleEnum userRoleEnum = user.getRole();

            switch (userRoleEnum) {
                case USER:
                    if (StringUtils.equals(memo.getUsername(), user.getUsername())) {
                        memo.update(requestDto);
                        return MemoResponseDto.Success(memo);
                    }
                    break;
                case ADMIN:
                    memo.update(requestDto);
                    return MemoResponseDto.Success(memo);
            }

            return MemoResponseDto.False();


        } catch (Exception e) {
            return MemoResponseDto.False();
        }
    }

    @Transactional
    public ResponseDto delete(Long id, User user) {
        try {
            Memo memo = memoRepository.findById(id).orElseThrow(
                    () -> new NullPointerException("존재하지 않는 게시글입니다.")
            );

            UserRoleEnum userRoleEnum = user.getRole();

            switch (userRoleEnum) {
                case USER:
                    if (StringUtils.equals(memo.getUsername(), user.getUsername())) {
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
}