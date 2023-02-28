package com.ecnu;

import com.ecnu.pojo.Comment;
import com.ecnu.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = EssayProjectApplication.class)
public class CommentTest {

    @Autowired
    CommentService commentService;

    @Test
    public void addComment(){
        Comment comment = new Comment();
        comment.setEssayId(Long.valueOf(1));
        comment.setUserId(Long.valueOf(3));
        comment.setContent("这是一个测试评论");
        commentService.addComment(comment);
    }

}
