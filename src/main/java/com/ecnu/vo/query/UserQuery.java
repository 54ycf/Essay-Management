package com.ecnu.vo.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserQuery extends BaseQuery{

    private String username;
    private Long userId;
    private String role;
    private Boolean deleted;
}
