package com.example.backend.config;

import com.example.backend.dto.CustomUser;
import com.example.backend.service.MyUsersDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final MyUsersDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        Cookie [] cookies = request.getCookies();
        if (cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 이름이 "jwt"인 쿠키를 발견한 경우, 변수에 저장
        var jwtCookie = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("jwt")) {
                jwtCookie = cookie.getValue();
            }
        }

        if (jwtCookie == null || jwtCookie.isEmpty()) {
            System.out.println("❌ JWT 쿠키 없음");
            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키에서 꺼낸 JWT가 유효한지 확인
        Claims claim;
        try {
            // extractToken() 안에 JWT를 입력하면 자동으로 까주는데 유효기간이 만료되거나 이상한 경우엔 에러를 내줄
            claim = jwtUtil.extractToken(jwtCookie);
        } catch (Exception e) {
            System.out.println("유효기간 만료되거나 이상함");
            filterChain.doFilter(request, response);
            return;
        }

        // JWT 안에 있는 내용 꺼내기 -> claim.get("displayName").toString()

        // JWT에서 유저 권한 정보 가져오기
        String authoritiesString = claim.get("roles", String.class);
        List<GrantedAuthority> authorities = Arrays.stream(authoritiesString.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        String username = claim.getSubject();
        if (username == null) {
            System.out.println("❌ username이 JWT에서 없음");
            filterChain.doFilter(request, response);
            return;
        }

        Long userId = ((Number) claim.get("userId")).longValue();
        System.out.println("✅ JWT Claim userId: " + claim.get("userId"));

        CustomUser customUser = new CustomUser(userId, username, "PROTECTED", authorities);

        // 인증 객체 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                customUser,
                null,
                customUser.getAuthorities()
        );

        System.out.println("JwtFilter.authToken = " + authToken);

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
