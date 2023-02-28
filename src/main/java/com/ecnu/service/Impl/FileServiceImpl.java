package com.ecnu.service.Impl;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import com.ecnu.service.FileService;
import com.ecnu.util.ThreadContextHolder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Value("${web-storage-root}")
    private String webStorageRoot;
    private final String localStorageRoot = System.getProperty("user.dir") + "/file";

    private final List<String> imgTypes = Arrays.asList("jpg","png");
    private final List<String> compressedTypes = Arrays.asList("zip", "rar", "jar");

    private String uploadFile(MultipartFile file, String type){
        if ((type.equals("img") || type.equals("profile")) && !isImg(file)){
            return null; //并非jpg或者png图片
        }else if (type.equals("attachment") && !isZip(file)){
            return null; //并非指定的压缩文件
        }

        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (suffix.length() == 1){
            return null; //没有后缀去指定类型
        }
        String uuid;
        if (type.equals("profile")){
            uuid = ThreadContextHolder.getUserInfo().getUserId().toString();
        }else{
            uuid = UUID.randomUUID().toString().replace("-","");
        }
        String fileNewName = uuid + suffix;
        Long uploaderId = ThreadContextHolder.getUserInfo().getUserId();
        String resourcePath = "/resource/" + type + '/' + uploaderId + '/' + fileNewName;
        String targetPath = localStorageRoot + resourcePath;
        try {
            FileUtil.writeBytes(file.getBytes(), targetPath);
            return webStorageRoot + resourcePath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public String uploadImg(MultipartFile file) {
        return uploadFile(file, "img");
    }

    @Override
    public String uploadProfile(MultipartFile file) {
        return uploadFile(file, "profile");
    }

    @Override
    public String uploadAttachment(MultipartFile file) {
        return uploadFile(file, "attachment");
    }


    @SneakyThrows
    public boolean isImg(MultipartFile file){
        String type = FileTypeUtil.getType(file.getInputStream());
        return imgTypes.contains(type);
    }
    @SneakyThrows
    public boolean isZip(MultipartFile file){
        String type = FileTypeUtil.getType(file.getInputStream());
        return compressedTypes.contains(type);
    }


    @SneakyThrows
    @Override
    public void getFileStream(String resourcePath, HttpServletResponse response) {
        ServletOutputStream os;

        String targetPath = localStorageRoot + resourcePath;
        String fileName = resourcePath.substring(resourcePath.lastIndexOf('/')+1);
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("application/octet-stream");
        byte[] bytes = FileUtil.readBytes(targetPath);
        os = response.getOutputStream();
        os.write(bytes);
        os.flush();
        os.close();
    }
}
