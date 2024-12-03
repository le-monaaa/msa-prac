package com.sparta.msa_exam.auth.controller;

import com.sparta.msa_exam.auth.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/token")
public class TokenController {

    private final JwtUtil jwtUtil;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtUtil.getTokenFromCookie(request, jwtUtil.REFRESH_TOKEN_COOKIE);
        String username = jwtUtil.getUserInfoFromToken(refreshToken).getSubject();

        if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {

            String newAccessToken = jwtUtil.createAccessToken(username);

            HttpHeaders headers = jwtUtil.createAccessTokenHeader(newAccessToken);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body("New access token issued");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }
}
