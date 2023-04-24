package com.sparta.hanghaemamo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomSecurityFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    //UserDetailsService 인터페이스를 구현한 클래스인 UserDetailsServiceImpl의 인스턴스를 주입받아 사용
    // 인증 시 사용자 정보를 로드하기 위해 사용
    private final PasswordEncoder passwordEncoder;
    // passwordEncoder는 비밀번호를 암호화하기 위한 Spring Security의 PasswordEncoder 인터페이스를 구현한 클래스의 인스턴스를 주입받아 사용


    @Override
    // 실제 요청에 대한 처리 로직을 작성하는 메소드
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("username = " + username);
        System.out.println("password = " + password);
        System.out.println("request.getRequestURI() = " + request.getRequestURI());


        // 요청 URI가 "/api/user/login" 또는 "/api/test-secured" 인 경우에만 로그인 검증을 수행
        if(username != null && password  != null && (request.getRequestURI().equals("/api/user/login") || request.getRequestURI().equals("/api/test-secured"))){
            // userDetailsService를 사용하여 username을 기반으로 UserDetails 객체를 가져옴
            // username을 인자로 받아 해당 사용자의 정보를 조회하여 UserDetails 인터페이스를 구현한 객체를 반환
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 비밀번호 확인
            if(!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new IllegalAccessError("비밀번호가 일치하지 않습니다.");
            }

            // 인증 객체 생성 및 등록
            // 인증된 사용자의 정보를 SecurityContext에 등록하고, 이후 권한 검사 등의 작업을 수행할 수 있
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            //userDetails: 사용자 정보를 담고 있는 UserDetails 객체
            //null: 인증 요청 시 제공되는 자격 증명 외에 추가 정보를 제공하지 않으므로 null 값을 전달
            //userDetails.getAuthorities(): 해당 사용자의 권한 정보를 나타내는 객체들의 컬렉션을 반환하는 메소드
            context.setAuthentication(authentication);

            SecurityContextHolder.setContext(context);
            //SecurityContext 객체를 현재 스레드의 ThreadLocal에 등록
        }

        filterChain.doFilter(request,response);
        //다음 필터로 요청을 전달
    }
}
