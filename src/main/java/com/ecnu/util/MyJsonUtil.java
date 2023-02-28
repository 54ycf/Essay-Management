package com.ecnu.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class MyJsonUtil {
    @SneakyThrows
    public static String object2JsonStr(Object o){
        return new ObjectMapper().writeValueAsString(o);
    }

    @SneakyThrows
    public static Object JsonStr2Object(String str, Class target){
        return new ObjectMapper().readValue(str, target);
    }
}
