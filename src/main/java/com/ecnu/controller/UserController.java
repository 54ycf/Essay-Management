package com.ecnu.controller;

import com.ecnu.common.R;
import com.ecnu.service.UserService;
import com.ecnu.vo.params.LoginParams;
import com.ecnu.vo.params.RegisterParams;
import com.ecnu.vo.params.UpdatePwdParams;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public R login(@Validated @RequestBody LoginParams params){
        Map<String,Object> result = userService.login(params);
        if (result == null) return R.error();
        return R.putData(result);
    }

    @PostMapping("/register")
    public R register(@Validated @RequestBody RegisterParams params){
        if (userService.register(params)) return R.ok();
        return R.error("注册失败");
    }

    @PutMapping("/updatePwd")
    public R updatePwd(@Validated @RequestBody UpdatePwdParams params){
        if (userService.updatePwd(params)) {
            return R.ok();
        }
        return R.error("更新密码失败");
    }


}
