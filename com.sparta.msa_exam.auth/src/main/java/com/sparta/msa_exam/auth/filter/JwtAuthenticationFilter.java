package com.sparta.msa_exam.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.msa_exam.auth.domain.ResponseDto;
import com.sparta.msa_exam.auth.domain.SignInRequest;
import com.sparta.msa_exam.auth.security.UserDetailsImpl;
import com.sparta.msa_exam.auth.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "JwtAuthenticationFilter : 로그인, JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/auth/sign-in");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            SignInRequest requestDto = new ObjectMapper().readValue(request.getInputStream(), SignInRequest.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            logger.error("Error parsing LoginRequestDto in attemptAuthentication", e);
            return null;
        } catch (AuthenticationException e) {
            logger.error("Authentication error in attemptAuthentication", e);
            throw e;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getUsername();

        String accessToken = jwtUtil.createAccessToken(username);
        String refreshToken = jwtUtil.createRefreshToken(username);

        // 엑세스 토큰, 리프레시 토큰 추가
        HttpHeaders headers = jwtUtil.createAccessTokenHeader(accessToken);
        ResponseCookie refreshCookie = jwtUtil.createRefreshTokenCookie(refreshToken);

        // 헤더, 쿠키 응답에 추가
        response.setHeader("Authorization", headers.getFirst("Authorization"));
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        ResponseDto responseDto = new ResponseDto(ResponseDto.SUCCESS, "로그인 성공", null);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseDto));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String errorMsg;

        if (failed instanceof org.springframework.security.authentication.BadCredentialsException) {
            errorMsg = "이메일이나 비밀번호가 틀렸습니다";
            sendErrorResponse(response, ResponseDto.FAILURE, errorMsg);
        } else {
            errorMsg = "인증 실패";
            sendErrorResponse(response, ResponseDto.FAILURE, errorMsg);
        }

    }

    private void sendErrorResponse(HttpServletResponse response, Integer code, String msg) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ResponseDto<Object> responseDto = new ResponseDto<>(code, msg);
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseDto));
    }



}
