package com.ecnu.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface FileService {

    String uploadImg(MultipartFile file);

    String uploadProfile(MultipartFile file);

    void getFileStream(String uuid, HttpServletResponse response);

    String uploadAttachment(MultipartFile file);
}
