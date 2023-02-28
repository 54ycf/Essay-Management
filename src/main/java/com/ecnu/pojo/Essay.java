package com.ecnu.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Essay {

    private Long essayId;
    private String title;
    private String author;
    private String conference;
    private Date publishDate;
    private Long userId;
    private String digest;
    private String essayLink;
    private String essayType;
    private Long branchId;
    private Date createTime;
    private Boolean deleted;

}
