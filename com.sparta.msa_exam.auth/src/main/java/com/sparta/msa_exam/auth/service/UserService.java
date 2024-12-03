package com.sparta.msa_exam.auth.service;

import com.sparta.msa_exam.auth.doma.SignInRequest;
import com.sparta.msa_exam.auth.domain.ResponseDto;
import com.sparta.msa_exam.auth.domain.User;
import com.sparta.msa_exam.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public ResponseDto signUp(SignInRequest request) {

        // username 유효성 체크
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            return new ResponseDto(ResponseDto.SUCCESS, "이미 존재하는 username 입니다", null);
        }
        String endcodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .username(request.getUsername())
                .password(endcodedPassword)
                .build();
        userRepository.save(user);

        return new ResponseDto(ResponseDto.SUCCESS, "회원가입 성공", null);
    }

}
