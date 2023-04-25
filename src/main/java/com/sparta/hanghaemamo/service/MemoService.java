package com.sparta.hanghaemamo.service;

import com.sparta.hanghaemamo.dto.MemoCommentResponseDto;
import com.sparta.hanghaemamo.dto.MemoRequestDto;
import com.sparta.hanghaemamo.dto.MemoResponseDto;
import com.sparta.hanghaemamo.dto.ResponseDto;
import com.sparta.hanghaemamo.entity.Comment;
import com.sparta.hanghaemamo.entity.Memo;
import com.sparta.hanghaemamo.entity.User;
import com.sparta.hanghaemamo.entity.UserRoleEnum;
import com.sparta.hanghaemamo.repository.MemoRepository;
import com.sparta.hanghaemamo.security.UserDetailsImpl;
import com.sparta.hanghaemamo.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;
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

//        Memo memo = new Memo(requestDto);
//        String chkToken = jwtUtil.resolveToken(request);
//        Claims test = jwtUtil.getUserInfoFromToken(chkToken);
//        memo.setUsername(test.getSubject());
//
//        if (jwtUtil.validateToken(chkToken)) {
//            memoRepository.save(memo);
//            return MemoResponseDto.Success(memo);
//        } else
//            return MemoResponseDto.False();

        Memo memo = new Memo(requestDto);
        memo.setUsername(user.getUsername());
        memoRepository.saveAndFlush(memo);
        return MemoResponseDto.Success(memo);
    }

//    @Transactional(readOnly = true)
//    public MemoResponseDto<List<MemoCommentResponseDto>> getMemos() {
////        Set<Memo> memo = memoRepository.findAllMemoAndComments();
//        List<MemoCommentResponseDto> memo = memoRepository.findCommentsAndMemos();
//
//        return MemoResponseDto.Success(memo);
//    }

    @Transactional(readOnly = true)
    public List<MemoCommentResponseDto> getMemos() {
//        Memo memo = memoRepository.findById().orElseThrow(
//                () -> new NullPointerException("존재하지 않는 게시글입니다.")
//        );
        List<Comment> comments = commentService.getAllComments();
//        List<MemoCommentResponseDto> test = memoRepository.findAll(Sort.by(Sort.Direction.DESC, "ModifiedAt"));
//        List<MemoCommentResponseDto> commentsAndMemo = memoRepository.findAllOrderByModifiedAtDesc();
//        return new MemoCommentResponseDto(memo.getId(), memo.getUsername(), memo.getContents(), memo.getContentName(), comments);
    }



//    @Transactional(readOnly = true)
//    public MemoResponseDto<List<MemoCommentResponseDto>> searchMemos(Long id) {
////        Set<Memo> memo = memoRepository.findMemoAndComments(id);
//        List<MemoCommentResponseDto> memo = memoRepository.findCommentAndMemo(id);
//        return MemoResponseDto.Success(memo);
//    }


    @Transactional
//    @PostAuthorize("isAuthenticated() and (hasRole('ADMIN') or #user.username == authentication.principal)")
    //ROLE_USER ROLE_ADMIN
//    @PreAuthorize("#user.username == principal.username ")
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

//            if (userRoleEnum == UserRoleEnum.USER) {
//                if (StringUtils.equals(memo.getUsername(), user.getUsername())) {
//                    memo.update(requestDto);
//                    return MemoResponseDto.Success(memo);
//                }
//            }
//

//            String token = jwtUtil.resolveToken(request);
//
//            if (token != null) {
//                if (jwtUtil.validateToken(token)) {
//                    Claims claims = jwtUtil.getUserInfoFromToken(token);
//                    String username = claims.get(SUBJECT_KEY, String.class);
//                    String role = claims.get(AUTHORIZATION_KEY, String.class);
//                    if (role == UserRoleEnum.USER.name()) {
//                        if (StringUtils.equals(memo.getUsername(), username)) {
//                            // StringUtils.equals를 쓴 이유는 Null-safe하게 사용하기 위해서 -> Java가 잘못함
//                            memo.update(requestDto);
//                            return MemoResponseDto.Success(memo);
//                        }
//                    } else {
//                        memo.update(requestDto);
//                        return MemoResponseDto.Success(memo);
//                    }
//                }
//            }
//            return MemoResponseDto.False();

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


//            UserRoleEnum userRoleEnum = user.getRole();
//
//            if (userRoleEnum == UserRoleEnum.USER) {
//                if (StringUtils.equals(memo.getUsername(), user.getUsername())) {
//                    memoRepository.deleteById(id);
//                    return new ResponseDto("게시글 삭제 성공했습니다!!!", HttpStatus.OK);
//                }
//            }
//            memoRepository.deleteById(id);
//            return new ResponseDto("게시글 삭제 성공했습니다!!!", HttpStatus.OK);

//            String token = jwtUtil.resolveToken(request);

//            if (token != null) {
//                if (jwtUtil.validateToken(token)) {
//                    Claims claims = jwtUtil.getUserInfoFromToken(token);
//                    String username = claims.get(SUBJECT_KEY, String.class);
//                    String role = claims.get(AUTHORIZATION_KEY, String.class);
//                    if (StringUtils.equals(role, UserRoleEnum.USER.name())) {
//                        if (StringUtils.equals(memo.getUsername(), username)) {
//                            memoRepository.deleteById(id);
//                            return new ResponseDto("게시글 삭제 성공했습니다!!!", HttpStatus.OK);
//                        }
//                    } else {
//                        memoRepository.deleteById(id);
//                        return new ResponseDto("게시글 삭제 성공했습니다!!!", HttpStatus.OK);
//                    }
//                }
//            }
//            return new ResponseDto("토큰이 없어 게시글 삭제 실패했습니다!!!", HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            return new ResponseDto("오류로 실패했습니다!!!", HttpStatus.BAD_REQUEST);
        }
    }
}