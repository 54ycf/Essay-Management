package com.ecnu.service.Impl;

import com.ecnu.dao.CommentDao;
import com.ecnu.pojo.Comment;
import com.ecnu.service.CommentService;
import com.ecnu.service.EssayService;
import com.ecnu.service.UserService;
import com.ecnu.util.ThreadContextHolder;
import com.ecnu.vo.CommentVo;
import com.ecnu.vo.EssayVo;
import com.ecnu.vo.query.CommentQuery;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;

    private final UserService userService;

    @Autowired
    private EssayService essayService;

    @Override
    public Comment getById(Long commentId) {
        return commentDao.getById(commentId);
    }

    @Override
    public List<Comment> listComment() {
        return commentDao.listComment();
    }

    @Override
    public PageInfo<Comment> queryComment(CommentQuery query) {
        if (!ThreadContextHolder.getUserInfo().getRole().equals("S")) return null; //非管理员无权限
        PageHelper.startPage(query.getPageNum(), 20);
        return new PageInfo<>(commentDao.queryComment(query));
    }


    @Override
    public int addComment(Comment comment) {
        comment.setUserId(ThreadContextHolder.getUserInfo().getUserId());
        EssayVo essayDetail = essayService.getEssayDetail(comment.getEssayId());
        Assert.notNull(essayDetail, "文章不存在");
        if (comment.getParentCommentId()!=null){ //代表这个人评论了一个评论
            Comment parentComment = getById(comment.getParentCommentId());
            Assert.notNull(parentComment, "parent comment must exists");
            Assert.isTrue(parentComment.getEssayId().equals(comment.getEssayId()), "文章不一致");
            if(parentComment.getRootCommentId() == null){
                // 父评论是顶层评论时
                comment.setRootCommentId(parentComment.getCommentId());
            } else {
                // 父评论不是顶层评论时，根评论ID与父评论一致
                comment.setRootCommentId(parentComment.getRootCommentId());
            }
        }
        return commentDao.addComment(comment);
    }

    @Override
    public boolean isCommentExists(Long commentId) {
        return commentDao.isCommentExists(commentId);
    }

    @Override
    public long updateCommentDeleted(Long commentId, Boolean deleted) {
        if (!getById(commentId).getUserId().equals(ThreadContextHolder.getUserInfo().getUserId()) && !ThreadContextHolder.getUserInfo().getRole().equals("S")) return 0; //只有自己和管理员有删除权限
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setDeleted(deleted);
        return commentDao.updateCommentDeleted(comment);
    }

    @Override
    public boolean deleteById(Long commentId) {
        return updateCommentDeleted(commentId, true) > 0;
    }

    @Override
    public List<CommentVo> listCommentsByEssayId(Long essayId){
        return commentDao.listCommentsByEssayId(essayId);
    }
}
