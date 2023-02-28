package com.ecnu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "essayId")
public class ESEssay {

    private Long essayId;

    private String title;

    private String conference;

    private String digest;

    private Boolean deleted;
}
