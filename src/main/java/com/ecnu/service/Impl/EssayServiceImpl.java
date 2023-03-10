package com.ecnu.service.Impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.ecnu.dao.EssayDao;
import com.ecnu.pojo.ESEssay;
import com.ecnu.service.ESEssayService;
import com.ecnu.service.EssayService;
import com.ecnu.util.ThreadContextHolder;
import com.ecnu.vo.*;
import com.ecnu.vo.query.EssayQuery;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class EssayServiceImpl implements EssayService {
    @Autowired
    EssayDao essayDao;

    @Autowired
    ESEssayService esEssayService;

    private final String localStorageRoot = System.getProperty("user.dir") + "/file";

    @Override
    public PageInfo<EssayItemVo> findEssayResults(EssayQuery query) {
        if (!StrUtil.isBlank(query.getKeyword()) || !StrUtil.isBlank(query.getConference())){
            List<Long> index = esEssayService.searchIndex(query);
            if (index==null || index.isEmpty()) return null;
            query.setIndices(index);
        }
        System.out.println(query.getIndices());
        PageHelper.startPage(query.getPageNum(), 10);
        PageInfo<EssayItemVo> result = new PageInfo<>(essayDao.listEssayItems(query));
        for (EssayItemVo item : result.getList()) {
            String essayId = item.getEssayId().toString();
            ESEssay essay = esEssayService.get(essayId);
            BeanUtils.copyProperties(essay, item);
        }
        return result;
    }

    @Override
    public EssayVo getEssayDetail(Long essayId) {
        EssayVo essayDetail = essayDao.getEssayDetail(essayId);
        if (essayDetail == null)return null;
        List<Map<String, Object>> maps = essayDao.listEssayReferences(essayId);
        essayDetail.setReferences(maps);

        String filename = "essay_" + essayId + ".txt";
        String resourcePath = "/essay/" + filename;
        String targetPath = localStorageRoot + resourcePath;
        byte[] bytes = FileUtil.readBytes(targetPath);
        String content = new String(bytes, StandardCharsets.UTF_8);
        essayDetail.setContent(content);

        ESEssay essay = esEssayService.get(essayId.toString());
        BeanUtils.copyProperties(essay, essayDetail);
        return essayDetail;
    }

    @Override
    public List<DirectionVo> listAllDirections() {
        return essayDao.listAllDirections();
    }

    @Override
    public List<Map<String, Object>> listAccurateTitles(String key) {
        return esEssayService.searchTitles(key);
//        return essayDao.listTitlesByKey(key);
    }

    @Override
    public PageInfo<EssayItemVo> listUserEssay(EssayQuery query) {
        return findEssayResults(query);
    }

    @Transactional
    @Override
    public void addEssay(EssayUploadVo essayUpload) {
        essayUpload.setUserId(ThreadContextHolder.getUserInfo().getUserId());
        essayUpload.setEssayId(null);
        essayUpload.setCreateTime(new Date());
        essayDao.addEssay(essayUpload);//??????MySQL?????????????????????????????????????????????essayId??????

        if (essayUpload.getReferenceIds() != null && !essayUpload.getReferenceIds().isEmpty()) {
            essayDao.addReferenceIds(essayUpload);
        }//????????????

        ESEssay essay = new ESEssay();
        BeanUtils.copyProperties(essayUpload,essay);
        esEssayService.addDocument(essay);//ElasticSearch??????

        String filename = "essay_" + essayUpload.getEssayId() + ".txt";
        String resourcePath = "/essay/" + filename;
        String targetPath = localStorageRoot + resourcePath;
        FileUtil.writeBytes(essayUpload.getContent().getBytes(), targetPath);
    }

    @Transactional
    @Override
    public boolean updateEssay(EssayUploadVo essayUpload) {
        Boolean deleted = essayDao.isEssayDeleted(essayUpload.getEssayId());
        if (deleted==null || deleted){
            return false;
        }
        if (!essayDao.getOwnerId(essayUpload.getEssayId()).equals(ThreadContextHolder.getUserInfo().getUserId())){
            return false;
        }//??????????????????????????????????????????

        essayDao.updateEssay(essayUpload);
        if (essayUpload.getReferenceIds() != null && !essayUpload.getReferenceIds().isEmpty()) {
            essayDao.deleteReferenceIds(essayUpload);
            essayDao.addReferenceIds(essayUpload);//??????????????????
        }

        String filename = "essay_" + essayUpload.getEssayId() + ".txt";
        String resourcePath = "/essay/" + filename;
        String targetPath = localStorageRoot + resourcePath;
        FileUtil.writeBytes(essayUpload.getContent().getBytes(), targetPath);//??????txt??????

        ESEssay essay = new ESEssay();
        BeanUtils.copyProperties(essayUpload,essay);
        esEssayService.updateDocument(essay);
        return true;
    }

    @Transactional
    @Override
    public boolean deleteEssay(Long essayId) {//????????????????????????????????????
        if (essayDao.getOwnerId(essayId).equals(ThreadContextHolder.getUserInfo().getUserId()) || ThreadContextHolder.getUserInfo().getRole().equals("S")) {
            essayDao.deleteEssay(essayId);
            esEssayService.deleteDocument(essayId);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateDirection(DirectionManageVo directionManageVo) {
        if (!ThreadContextHolder.getUserInfo().getRole().equals("S")){
            return false;
        }
        return essayDao.updateDirection(directionManageVo)>0;
    }

    @Override
    public boolean deleteDirection(DirectionManageVo directionManageVo) {
        if (!ThreadContextHolder.getUserInfo().getRole().equals("S")){
            return false;
        }
        return essayDao.deleteDirection(directionManageVo)>0;
    }

    @Override
    public Map<String,Object> getUserEssayStatistics(Long userId) {

        List<Map<String, Object>> groupList = essayDao.listUserEssayGroups(userId);
        if (groupList.size() > 7){
            //?????????????????????????????????????????????????????????????????????
            Long sum = 0L;
            while (groupList.size()>7){
                Long num = (Long) groupList.get(7).get("value");
                sum += num;
                groupList.remove(7);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("name","??????");
            map.put("value",sum);
            groupList.add(map);
        }

        List<Map<String, Object>> lastSixMonthEssay = essayDao.getLastSixMonthEssay(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("group", groupList);
        result.put("sixMonth",lastSixMonthEssay);
        return result;
    }


}
