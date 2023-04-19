package com.sparta.hanghaemamo.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    @Size(min = 4, max = 10, message = "id는 4 이상, 10 이하만 가능합니다.")
    @Pattern(regexp =  "/[a-z0-9]/", message = "id는 소문자, 숫자만 가능합니다.")
    @NotNull(message = "id를 입력해주세요")
    private String username;
    @Size(min = 8, max = 15, message = "password는 8 이상, 15 이하만 가능합니다.")
    @Pattern(regexp =  "/[a-zA-Z0-9]/", message = "password는 소문자, 숫자만 가능합니다.")
    @NotNull(message = "password를 입력해주세요")
    private String password;
}
