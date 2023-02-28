package com.ecnu.dao;

import com.ecnu.vo.*;
import com.ecnu.vo.query.EssayQuery;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface EssayDao {

    List<EssayItemVo> listEssayItems(EssayQuery query);

    //获取论文详情
    EssayVo getEssayDetail(Long essayId);

    //获取一篇文章的引用文章
    List<Map<String,Object>> listEssayReferences(Long essayId);

    //获取所有的方向
    List<DirectionVo> listAllDirections();

    //通过关键字去查找论文
    List<Map<String,Object>> listTitlesByKey(String key); //期望用ES实现

    //查看文章是否已经删除
    Boolean isEssayDeleted(Long essayId);

    Long getOwnerId(Long essayId);

    //获取一个用户的写的笔记的分类情况
    List<Map<String,Object>> listUserEssayGroups(Long userId);

    //获取一个用户近六个月的上传笔记数量
    List<Map<String,Object>> getLastSixMonthEssay(Long userId);




    int addEssay(EssayUploadVo essay);

    int addReferenceIds(EssayUploadVo referenceIds);



    int updateEssay(EssayUploadVo essay);

    int deleteReferenceIds(EssayUploadVo referenceIds);


    int deleteEssay(Long essayId);


    int updateDirection(DirectionManageVo directionManageVo);

    int deleteDirection(DirectionManageVo directionManageVo);
}
