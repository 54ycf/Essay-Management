package com.ecnu.vo.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UpdatePwdParams {
    @NotBlank
    private String oldPwd;
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}$", message = "密码必须包含大小写字母和数字的组合，可以使用特殊字符，长度在8-16之间。\n")
    private String newPwd;
}
