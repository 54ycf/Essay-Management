package com.ecnu.vo.query;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.util.Date;
import java.util.List;

/*
查找论文的时候的条件整合成一个类
 */
@Data
public class EssayQuery {
    private String keyword;
    private Long classificationId;
    private Long groupId;
    private Long branchId;
    private String essayType;
    private String author;
    private String ownerName;
    private Long userId;
    private String conference;
    @DateTimeFormat(pattern = "yyyy-MM")
    private Date createTime;
    private Integer pageNum;

    @Null
    private List<Long> indices;
}
