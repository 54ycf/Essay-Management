package com.ecnu.vo;

import com.ecnu.pojo.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CommentManageVo extends Comment {
    private String username;
}
