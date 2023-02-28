package com.ecnu.vo;

import lombok.Data;

import java.util.Date;

//搜索请求中的论文列表的每一项的数据
@Data
public class EssayItemVo {
    private Long essayId;
    private String title;
    private String author;
    private String conference;
    private Date publishDate;
    private String essayType;
    private String digest;
    private String essayLink;
    private String username;
    private Date createTime;
}
