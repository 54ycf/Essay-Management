package com.ecnu.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class EssayVo {
    private Long essayId;
    private String title;
    private String author;
    private String conference;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishDate;
    private String essayType;
    private Long branchId;
    private String digest;
    private String essayLink;
    private String username;
    private Long userId;
    private Date createTime;

    private List<Map<String,Object>> references;
//    private List<Long> referenceIds;

    private String content;
    private String attachment;
}
