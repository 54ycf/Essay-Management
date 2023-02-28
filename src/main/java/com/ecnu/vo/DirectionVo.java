package com.ecnu.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DirectionVo {
    private Long value;
    private String label;
    private List<DirectionVo> children;
}
