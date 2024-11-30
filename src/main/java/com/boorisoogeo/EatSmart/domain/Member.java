package com.boorisoogeo.EatSmart.domain;

import jakarta.validation.constraints.*;

import lombok.Data;


import java.time.LocalDate;

@Data
public class Member {


    private Long id;//db에 저장되고 관리되는 아이디

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    @Pattern(regexp = "^[a-z0-9]{4,20}$", message = "아이디는 영문 소문자와 숫자 4~12자리여야 합니다.")
    private String loginId;//로그인 id

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;
    private String confirmPassword;

    @NotEmpty(message = "이름 입력은 필수입니다.")
    private String name;//사용자 이름
    @Email @NotEmpty(message = "이메일 입력은 필수입니다.")
    private String email;

    @NotNull(message = "성별 입력은 필수입니다.")
    private String gender;

    @NotNull(message = "생년월일 입력은 필수입니다.")
    private LocalDate date;

    private Double height;
    private Double weight;
}