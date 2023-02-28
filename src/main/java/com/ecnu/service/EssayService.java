package com.ecnu.service;

import com.ecnu.vo.*;
import com.ecnu.vo.query.EssayQuery;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;


public interface EssayService {

    PageInfo<EssayItemVo> findEssayResults(EssayQuery params);

    EssayVo getEssayDetail(Long essayId);

    Map<String,Object> getUserEssayStatistics(Long userId);

    List<DirectionVo> listAllDirections();

    List<Map<String, Object>> listAccurateTitles(String key);

    PageInfo<EssayItemVo> listUserEssay(EssayQuery params);

    void addEssay(EssayUploadVo essayNote);

    boolean updateEssay(EssayUploadVo essayNote);

    boolean deleteEssay(Long essayId);


    boolean updateDirection(DirectionManageVo directionManageVo);

    boolean deleteDirection(DirectionManageVo directionManageVo);

}
