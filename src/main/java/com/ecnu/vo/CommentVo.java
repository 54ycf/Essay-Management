package com.ecnu.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentVo {

    private Long commentId;
    private String content;
    private Long userId;
    private Date time;
    private Long pid;
    private String pname;
    private Long originId;
    private Long essayId;
    private String username;
    private Boolean deleted;

    private List<CommentVo> children;
}
