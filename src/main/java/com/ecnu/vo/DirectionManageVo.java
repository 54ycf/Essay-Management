package com.ecnu.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DirectionManageVo {
    private Long classificationId;
    private Long groupId;
    private Long branchId;
    @NotBlank
    private String name;
}
