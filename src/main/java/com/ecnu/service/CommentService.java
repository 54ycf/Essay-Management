package com.ecnu.service;

import com.ecnu.pojo.Comment;
import com.ecnu.vo.CommentVo;
import com.ecnu.vo.query.CommentQuery;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CommentService {

    Comment getById(Long commentId);

    List<Comment> listComment();

    /**
     * 查询符合条件的评论
     * @param query /
     * @return 评论
     */
    PageInfo<Comment> queryComment(CommentQuery query);


    int addComment(Comment comment);

    /**
     * 判断评论是否存在
     * @param commentId /
     * @return /
     */
    boolean isCommentExists(Long commentId);

    /**
     * 更新评论删除状态
     * @return /
     */
    long updateCommentDeleted(Long commentId, Boolean deleted);

    /**
     * 删除评论
     * @param commentId /
     * @return /
     */
    boolean deleteById(Long commentId);

    List<CommentVo> listCommentsByEssayId(Long essayId);
}
