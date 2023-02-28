package com.ecnu.dao;
import com.ecnu.pojo.User;

import com.ecnu.vo.UserVo;
import com.ecnu.vo.query.UserQuery;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper //告诉springboot这是一个mybatis类
@Repository
public interface UserDao {

    //查询用户名是否重复
    boolean isUsernameExist(String username);

    //添加一个用户，数据库主键自增
    int addUser(User user);

    //根据用户名去查询用户信息
    User findUserByName(String username);

    //根据用户id去查找用户信息
    User findUserById(Long userId);

    //更新用户密码
    int updatePwd(User user);


    /**
     * 用户总数
     * @return 用户总数
     */
    long count();

    /**
     * 根据条件查询用户
     * @param query /
     * @return /
     */
    List<UserVo> queryUser(UserQuery query);

    /**
     * 查询符合条件的用户数量，用户分页
     * @param query /
     * @return /
     */
    long queryUserCount(UserQuery query);

    /**
     * 更新用户是否被删除的状态
     * @param user user只需包含id和deleted
     */
    long updateUserDeleted(User user);
}
