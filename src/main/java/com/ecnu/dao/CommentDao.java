package com.ecnu.dao;

import com.ecnu.pojo.Comment;
import com.ecnu.vo.query.CommentQuery;
import com.ecnu.vo.CommentVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentDao {

    Comment getById(Long commentId);

    List<Comment> listComment();

    /**
     * 查询符合条件的评论
     * @param query /
     * @return 评论
     */
    List<Comment> queryComment(CommentQuery query);


    int addComment(Comment comment);

    /**
     * 判断评论是否存在
     * @param commentId /
     * @return /
     */
    boolean isCommentExists(Long commentId);

    /**
     * 更新评论删除状态
     * @param comment 只需包含commentId和deleted字段
     * @return /
     */
    long updateCommentDeleted(Comment comment);

    List<CommentVo> listCommentsByEssayId(Long essayId);
}
