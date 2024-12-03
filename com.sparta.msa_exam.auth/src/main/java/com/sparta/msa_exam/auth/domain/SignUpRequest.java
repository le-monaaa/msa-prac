package com.sparta.msa_exam.auth.domain;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@Data
@Builder
public class SignUpRequest {

    @NotBlank
    private String username;

    private String password;
}
