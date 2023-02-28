package com.ecnu;

import com.ecnu.dao.UserDao;
import com.ecnu.service.UserService;
import com.ecnu.vo.UserVo;
import com.ecnu.vo.params.RegisterParams;
import com.ecnu.vo.query.UserQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.UUID;

@SpringBootTest(classes = EssayProjectApplication.class)
public class UserManageTest {

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    @Test
    public void insert(){
        long p = userDao.count();
        for (int i = 0; i < 10; i++) {
            RegisterParams registerParams = new RegisterParams();
            registerParams.setEmail(UUID.randomUUID().toString().replaceAll("-", "") + "@asd.com");
            registerParams.setUsername(UUID.randomUUID().toString().replaceAll("-", ""));
            registerParams.setRealName(UUID.randomUUID().toString().replaceAll("-", ""));
            registerParams.setPassword("ab123456");
            userService.register(registerParams);
        }
        Assert.isTrue(userService.count() - p == 10L, "warn");
    }

    @Test
    public void delete(){
//        List<User> users = userService.listUser();
//        if(!users.isEmpty()){
//            User user = users.get(0);
//            userService.updateUserDeleted(user.getUserId(), true);
//            Assert.isNull(userService.getById(user.getUserId()));
//            Assert.isTrue(userService.count() == users.size() - 1);
//        }
    }

    @Test
    @Transactional
    public void query(){
        final int batchSize = 20;
        final int pageSize = 5;
        long c = System.currentTimeMillis();
        for (int i = 0; i < batchSize; i++) {
            RegisterParams registerParams = new RegisterParams();
            registerParams.setEmail(UUID.randomUUID().toString().replaceAll("-", "") + "@asd.com");
            registerParams.setUsername("PREFIX-" + c + "-QUERY-USERNAME-" + i);
            registerParams.setRealName(UUID.randomUUID().toString().replaceAll("-", ""));
            registerParams.setPassword("ab123456");
            userService.register(registerParams);
        }
        for (int i = 0; i < batchSize / pageSize; i++) {
            UserQuery query = new UserQuery();
            query.setPageNum(i+1);
            query.setPageSize(pageSize);
            query.setUsername("%" + c + "-QUERY-USERNAME-%");
//            PageDTO<UserVo> page = userService.listUser(query);
//            Assert.isTrue(page.getTotal() == batchSize);
//            Assert.isTrue(!page.getContent().isEmpty());
//            Assert.isTrue(page.getContent().get(0).getUsername().equals("PREFIX-" + c + "-QUERY-USERNAME-" + (i * pageSize)));
        }

        UserQuery query = new UserQuery();
        query.setPageSize(1);
        query.setUsername("%" + c + "-QUERY-USERNAME-%");
//        PageDTO<UserVo> page = userService.listUser(query);
//        long total = page.getTotal();
//        userService.updateUserDeleted(page.getContent().get(0).getUserId(), true);
//
//        query.setDeleted(false);
//        PageDTO<UserVo> newPage = userService.listUser(query);
//        Assert.isTrue(newPage.getTotal() == total - 1);
    }
}
