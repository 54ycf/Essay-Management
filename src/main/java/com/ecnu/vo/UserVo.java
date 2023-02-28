package com.ecnu.vo;

import lombok.Data;


@Data
public class UserVo {
    private Long userId;
    private String username;
    private String realName;
    private String email;
    private String role;
    private Boolean deleted;
}
