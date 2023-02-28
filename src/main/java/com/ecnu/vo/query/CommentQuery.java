package com.ecnu.vo.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
public class CommentQuery extends BaseQuery{

    private String username;
    private Long essayId;
    @DateTimeFormat(pattern = "yyyy-MM")
    private Date createTime;
    private Boolean deleted;

}
