package com.ecnu.pojo;

import lombok.Data;

import java.math.BigInteger;

@Data
public class Direction {
    private BigInteger directionId;
    private String classification;
    private String group;
    private String branch;
}
