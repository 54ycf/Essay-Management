package com.ecnu.service.Impl;

import com.ecnu.dao.UserDao;
import com.ecnu.pojo.User;
import com.ecnu.service.UserService;
import com.ecnu.util.MySecurityUtil;
import com.ecnu.util.ThreadContextHolder;
import com.ecnu.vo.UserInfoVo;
import com.ecnu.vo.UserVo;
import com.ecnu.vo.params.LoginParams;
import com.ecnu.vo.params.RegisterParams;
import com.ecnu.vo.params.UpdatePwdParams;
import com.ecnu.vo.query.UserQuery;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public Map<String, Object> login(LoginParams params) {
        User user = userDao.findUserByName(params.getUsername());
        if (user==null || MySecurityUtil.isMisMatch(params.getPassword(), user.getSalt(), user.getPassword())) return null;

        HashMap<String, Object> map = new HashMap<>();
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user,userVo);
        map.put("user", userVo);
        UserInfoVo userInfo = new UserInfoVo();
        BeanUtils.copyProperties(user,userInfo); //userInfo里面是重要的不敏感信息,id,username,role
        map.put("token",MySecurityUtil.createJwtStr(userInfo));

        return map;
    }

    @Override
    public boolean register(RegisterParams params) {
        if (userDao.isUsernameExist(params.getUsername())) return false;

        User user = new User();
        BeanUtils.copyProperties(params, user); //拷贝属性到user
        String salt = MySecurityUtil.generateRandomString(); //生成一个随机字符串作为盐值
        user.setSalt(salt);
        user.setPassword(MySecurityUtil.encodeWithSalt(user.getPassword(), salt));
        userDao.addUser(user);

        return true;
    }

    @Override
    public boolean updatePwd(UpdatePwdParams params) {
        Long userId = ThreadContextHolder.getUserInfo().getUserId();
        User user = userDao.findUserById(userId);
        if (MySecurityUtil.isMisMatch(params.getOldPwd(), user.getSalt(), user.getPassword())){
            return false; //原始密码不对
        }
        String salt = MySecurityUtil.generateRandomString();
        String newEncodePwd = MySecurityUtil.encodeWithSalt(params.getNewPwd(), salt);
        user.setSalt(salt);
        user.setPassword(newEncodePwd);
        user.setUserId(userId);
        userDao.updatePwd(user);//更新密文与新的盐值
        return true;
    }

    @Override
    public User getById(Long userId) {
        return userDao.findUserById(userId);
    }

    @Override
    public long count() {
        return userDao.count();
    }

    @Override
    public PageInfo<UserVo> listUser(UserQuery query) {
        if (!ThreadContextHolder.getUserInfo().getRole().equals("S")) {
            return null; //非管理员无权限
        }
        PageHelper.startPage(query.getPageNum(), 20);
//        List<UserVo> users = userDao.queryUser(query);
//        long count = userDao.queryUserCount(query);
//        return new PageDTO<>(users, count);
        return new PageInfo<>(userDao.queryUser(query));
    }

    @Override
    public boolean updateUserDeleted(Long userId, Boolean deleted) {
        if (getById(userId).getRole().equals("S")) return false; //如果被修改对象同样为管理员，则无权限
        User user = new User();
        user.setUserId(userId);
        user.setDeleted(deleted);
        userDao.updateUserDeleted(user);
        return true;
    }

    @Override
    public boolean deleteById(Long userId) {
        return updateUserDeleted(userId, true);
    }
}
