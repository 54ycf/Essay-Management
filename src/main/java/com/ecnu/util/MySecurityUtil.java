package com.ecnu.util;

import com.ecnu.vo.UserInfoVo;
import io.jsonwebtoken.*;
import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class MySecurityUtil {

    final static int[] randomSequence = new int[]{7,25,47,24,3,56,38,2,42,5,40,1,29,52,26,44,33,39,12,48,15,4,13,36,27,46,22,0,6,21,53,11,31,16,8,30,9,50,19,20,54,23,41,45,32,43,34,55,14,17,10,49,18,35,37,51,28};

    public static String generateRandomString(){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<13;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    private static String encodeBySHA256(String origin){
        try {
            //确定计算方法
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            BASE64Encoder base64en = new BASE64Encoder();
            //加密后的字符串
            return base64en.encode(sha256.digest(origin.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeWithSalt(String origin, String salt){
        String encodeStr = encodeBySHA256(origin) + salt;
        char[] disorderStr = new char[57];
        for (int i = 0; i < disorderStr.length; i++) {
            disorderStr[i] = encodeStr.charAt(randomSequence[i]);
        }
        return encodeBySHA256(String.valueOf(disorderStr));
    }

    public static boolean isMisMatch(String origin, String salt, String encodePassword){
        return !encodeWithSalt(origin, salt).equals(encodePassword);
    }


    private static final long expiration = 1000L * 60L * 60L * 24L; // 每个token有效期24h
    private static final String mySignature = "fd8va0v3jg436uht4r3ngn3nve02efn20"; //自己的签名

    public static String createJwtStr(Object claim){
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .claim("user", MyJsonUtil.object2JsonStr(claim))
                .setSubject("essayAuthorization")
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .setId(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS256, mySignature)
                .compact();
    }

    public static UserInfoVo parseUser(String jwtToken) throws Exception{
        String userJsonStr = Jwts.parser()
                .setSigningKey(mySignature)
                .parseClaimsJws(jwtToken)
                .getBody()
                .get("user").toString();
        return (UserInfoVo) MyJsonUtil.JsonStr2Object(userJsonStr, UserInfoVo.class);
    }
}
