package com.ecnu.service;

import com.ecnu.pojo.ESEssay;
import com.ecnu.vo.query.EssayQuery;

import java.util.List;
import java.util.Map;

public interface ESEssayService {
    void addDocument(ESEssay essay);

    void deleteDocument(Long essayId);

    void recoverDocument(Long essayId);

    void updateDocument(ESEssay essay);

    List<Long> searchIndex(EssayQuery query);

    ESEssay get(String essayId);

    List<Map<String, Object>> searchTitles(String keyword);
}
