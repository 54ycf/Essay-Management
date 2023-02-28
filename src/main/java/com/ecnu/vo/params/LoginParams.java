package com.ecnu.vo.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginParams {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
