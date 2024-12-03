package com.sparta.msa_exam.auth.filter;

import com.sparta.msa_exam.auth.security.UserDetailsServiceImpl;
import com.sparta.msa_exam.auth.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JwtAuthorizationFilter: JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.getTokenFromHeader(request, jwtUtil.AUTHORIZATION_HEADER);

        if (StringUtils.hasText(accessToken)) {
            try {
                // 액세스 토큰이 유효하면 인증 설정
                if (jwtUtil.validateToken(accessToken)) {
                    setAuthentication(jwtUtil.getUserInfoFromToken(accessToken).getSubject());
                }
            } catch (ExpiredJwtException e) {
                // 액세스 토큰이 만료된 경우 리프레시 토큰 사용
                String refreshToken = jwtUtil.getTokenFromCookie(request, jwtUtil.REFRESH_TOKEN_COOKIE);
                if (StringUtils.hasText(refreshToken) && jwtUtil.validateToken(refreshToken)) {
                    String username = jwtUtil.getUserInfoFromToken(refreshToken).getSubject();

                    // 새로운 액세스 토큰 발급 및 헤더 추가
                    String newAccessToken = jwtUtil.createAccessToken(username);
                    HttpHeaders headers = jwtUtil.createAccessTokenHeader(newAccessToken);
                    response.setHeader(jwtUtil.AUTHORIZATION_HEADER, headers.getFirst(jwtUtil.AUTHORIZATION_HEADER));

                    // 새 액세스 토큰으로 인증 설정
                    setAuthentication(username);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
    // 인증 처리
    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
