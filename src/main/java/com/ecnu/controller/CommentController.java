package com.ecnu.controller;

import com.ecnu.common.R;
import com.ecnu.pojo.Comment;
import com.ecnu.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public R listCommentOfEssay(@RequestParam Long essayId){
        return R.putData(commentService.listCommentsByEssayId(essayId));
    }

    @PostMapping
    public R postComment(@Validated @RequestBody Comment comment){
        commentService.addComment(comment);
        return R.ok();
    }

    @DeleteMapping("/{commentId}")
    public R deleteComment(@PathVariable Long commentId){
        if (commentService.deleteById(commentId)) {
            return R.ok();
        }
        return R.error(-1,"无删除权限");
    }
}
