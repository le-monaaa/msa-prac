package com.sparta.msa_exam.auth.doma;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@Data
@Builder
public class SignInRequest {

    @NotBlank
    private String username;

    private String password;
}
