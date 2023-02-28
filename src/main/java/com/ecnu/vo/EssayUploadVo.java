package com.ecnu.vo;

import com.ecnu.common.group.Create;
import com.ecnu.common.group.Update;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;
import java.util.List;

@Data
public class EssayUploadVo {
    @Null(groups = Create.class)
    @NotNull(groups = Update.class)
    private Long essayId;
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    private String conference;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishDate;
    @Null
    private Long userId;
    @NotBlank
    private String essayType;
    @NotBlank
    private String digest;
    @NotBlank
    private String essayLink;
    @NotNull
    private Long branchId;
    @Null
    private Date createTime;
    @Null
    private Boolean deleted;

    private List<Long> referenceIds;

    @NotBlank
    private String content;
    private String attachment;
}
