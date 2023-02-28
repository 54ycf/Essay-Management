package com.ecnu.controller;

import com.ecnu.common.R;
import com.ecnu.pojo.Comment;
import com.ecnu.service.CommentService;
import com.ecnu.service.EssayService;
import com.ecnu.service.UserService;
import com.ecnu.vo.DirectionManageVo;
import com.ecnu.vo.UserVo;
import com.ecnu.vo.query.CommentQuery;
import com.ecnu.vo.query.UserQuery;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/manage")
@RequiredArgsConstructor
public class UserManageController {

    private final UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private EssayService essayService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping
    public R queryUser(UserQuery userQuery){
        PageInfo<UserVo> result = userService.listUser(userQuery);
        if (result == null)return R.error(-1,"无权限");
        return R.putData(result);
    }

    @DeleteMapping("remove_user/{id}")
    public R deleteUser(@PathVariable Long id){
        if (userService.deleteById(id)){
            return R.ok();
        }
        return R.error(-1, "无修改权限");
    }

    @GetMapping("/comment")
    public R listComment(CommentQuery query){
        PageInfo<Comment> result = commentService.queryComment(query);
        if (result == null) return R.error("无查看权限");
        return R.putData(result);
    }

    @PutMapping("/updateDirection")
    public R updateDirection(@RequestBody @Validated DirectionManageVo directionManageVo){
        if (essayService.updateDirection(directionManageVo)){
            redisTemplate.opsForValue().set("direction", essayService.listAllDirections().toString());//更新redis
            return R.ok();
        }
        return R.error(-1, "更新失败");
    }

    @DeleteMapping("/deleteDirection")
    public R deleteDirection(@RequestBody DirectionManageVo directionManageVo){
        System.out.println(directionManageVo);
        if (essayService.deleteDirection(directionManageVo)){
            return R.ok();
        }
        return R.error(-1, "删除失败");
    }
}
