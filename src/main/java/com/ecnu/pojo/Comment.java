package com.ecnu.pojo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

@Data
public class Comment {
    @Null
    private Long commentId;
    @NotNull
    private Long essayId;
    private Long userId;
    @Null
    private Long rootCommentId;
    private Long parentCommentId;
    @NotBlank
    private String content;
    @Null
    private Date createTime;
    @Null
    private Boolean deleted;
}
