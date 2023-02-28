package com.ecnu.vo.params;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
public class RegisterParams {

    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{3,15}$", message = "用户名应该字母开头，长度应该为4-16，允许字母数字下划线。\n")
    private String username;
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}$", message = "密码必须包含大小写字母和数字的组合，可以使用特殊字符，长度在8-16之间。\n")
    private String password;
    @Pattern(regexp = "[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*", message = "真实姓名应为2~5位非生僻汉字。\n")
    private String realName;
    @Email(message = "请填写正确的邮箱格式。\n")
    private String email;
}
