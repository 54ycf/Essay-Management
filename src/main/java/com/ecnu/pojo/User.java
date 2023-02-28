package com.ecnu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long userId;
    private String username;
    private String password;
    private String salt;
    private String realName;
    private String email;
    private String role;
    private Boolean deleted;

}
