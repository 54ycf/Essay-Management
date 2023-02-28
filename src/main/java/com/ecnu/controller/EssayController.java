package com.ecnu.controller;

import cn.hutool.core.util.StrUtil;
import com.ecnu.common.R;
import com.ecnu.common.group.Create;
import com.ecnu.common.group.Update;
import com.ecnu.service.EssayService;
import com.ecnu.vo.DirectionVo;
import com.ecnu.vo.EssayItemVo;
import com.ecnu.vo.EssayUploadVo;
import com.ecnu.vo.EssayVo;
import com.ecnu.vo.query.EssayQuery;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/essay")
@Validated
public class EssayController {

    @Autowired
    EssayService essayService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping
    public R findEssay(@Validated EssayQuery query){
        PageInfo<EssayItemVo> essayResults = essayService.findEssayResults(query);
        if (essayResults==null) return R.error("抱歉，未查到相关论文");
        return R.putData(essayResults);
    }

    @GetMapping("/detail")
    public R essayDetail(@RequestParam Long essayId){
        EssayVo essayDetail = essayService.getEssayDetail(essayId);
        if (essayDetail==null)return R.error("抱歉，不存在此论文，或已被删除");
        return R.putData(essayDetail);
    }

    @GetMapping("/directions")
    public R listAllDirections(){
        String directionVos = redisTemplate.opsForValue().get("direction");
//        List<DirectionVo> directionVos = essayService.listAllDirections();
        return R.putData(directionVos);
    }

    @GetMapping("/accurateTitle")
    public R getAccurateTitle(@RequestParam String key){
        if (StrUtil.isBlank(key)) return R.ok();
        List<Map<String, Object>> titles = essayService.listAccurateTitles(key);
        return R.putData(titles);
    }

    @GetMapping("/statistics")
    public R getEssayStatistics(@RequestParam Long userId){
        Map<String,Object> userEssayData = essayService.getUserEssayStatistics(userId);
        return R.putData(userEssayData);
    }

    @GetMapping("/user")
    public R getUserEssay(@Validated EssayQuery query){
        PageInfo<EssayItemVo> result = essayService.listUserEssay(query);
        if (result==null) return R.error("抱歉，未查到相关论文");
        return R.putData(result);
    }

    @PostMapping
    public R addEssay(@RequestBody @Validated(Create.class) EssayUploadVo essayUpload) {
        essayService.addEssay(essayUpload);
        return R.ok();
    }

    @PutMapping()
    public R updateEssay(@RequestBody @Validated(Update.class) EssayUploadVo essayUpload){
        if (essayService.updateEssay(essayUpload)) {
            return R.ok();
        }
        return R.error("更新错误");
    }

    @DeleteMapping("/{essayId}")
    public R deleteEssay(@PathVariable Long essayId) {
        if (essayService.deleteEssay(essayId)) {
            return R.ok();
        }
        return R.error("删除错误");
    }
}
