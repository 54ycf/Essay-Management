package com.ecnu.service;

import com.ecnu.pojo.User;
import com.ecnu.vo.UserVo;
import com.ecnu.vo.params.LoginParams;
import com.ecnu.vo.params.RegisterParams;
import com.ecnu.vo.params.UpdatePwdParams;
import com.ecnu.vo.query.UserQuery;
import com.github.pagehelper.PageInfo;


import java.util.Map;

public interface UserService {

    //用户登陆。失败返回null；成功返回用户信息以及token，封装在Map里
    Map<String,Object> login(LoginParams params);

    //用户注册，失败返回false，成功返回true
    boolean register(RegisterParams params);

    boolean updatePwd(UpdatePwdParams newPwd);

    User getById(Long userId);

    long count();

    PageInfo<UserVo> listUser(UserQuery query);

    boolean updateUserDeleted(Long userId, Boolean deleted);

    boolean deleteById(Long userId);
}
