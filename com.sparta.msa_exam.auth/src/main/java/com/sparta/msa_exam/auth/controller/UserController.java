package com.sparta.msa_exam.auth.controller;

import com.sparta.msa_exam.auth.domain.SignInRequest;
import com.sparta.msa_exam.auth.domain.SignUpRequest;
import com.sparta.msa_exam.auth.domain.ResponseDto;
import com.sparta.msa_exam.auth.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private UserService userService;

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseDto signUp(@RequestBody SignUpRequest request) {
        return userService.signUp(request);
    }


}
