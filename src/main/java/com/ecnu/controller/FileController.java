package com.ecnu.controller;

import com.ecnu.common.R;
import com.ecnu.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/img/upload")
    public R uploadImg(@RequestParam("file") MultipartFile file){
        String path = fileService.uploadImg(file);
        if (path != null) {
            return R.putData(path);
        }
        return R.error("img upload fail, 仅支持jpg和png图片格式, 请检查文件是否损坏");
    }
    @PostMapping("/profile/upload")
    public R uploadProfile(@RequestParam("file") MultipartFile file){
        String path = fileService.uploadProfile(file);
        if (path != null) {
            return R.putData(path);
        }
        return R.error("img upload fail, 仅支持jpg和png图片格式, 请检查文件是否损坏");
    }
    @PostMapping("/attachment/upload")
    public R uploadAttachment(@RequestParam("file") MultipartFile file){
        String path = fileService.uploadAttachment(file);
        if (path != null) {
            return R.putData(path);
        }
        return R.error("attachment upload fail, 建议使用Zip格式, 请检查文件是否损坏");
    }

    @GetMapping("/resource/{type}/{userId}/{filename}")
    public void getFile(@PathVariable String type,@PathVariable String userId, @PathVariable String filename, HttpServletResponse response) {
        String resourcePath = "/resource/" + type + '/' + userId + '/' + filename;
        fileService.getFileStream(resourcePath, response);
    }

}
