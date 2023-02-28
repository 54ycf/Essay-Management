package com.ecnu.pojo;

import lombok.Data;

import java.math.BigInteger;

@Data
public class Note {

    private BigInteger essayId;
    private String content;
    private String attachment;

}
