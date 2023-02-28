package com.ecnu.vo.query;

import lombok.Data;

@Data
public class BaseQuery {

    private Integer pageNum;

    private Integer pageSize;

    public BaseQuery() {
        pageNum = 1;//默认第一页
        pageSize = 10;//默认一页十条
    }
}
