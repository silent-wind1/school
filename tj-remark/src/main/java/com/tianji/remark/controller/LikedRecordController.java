package com.tianji.remark.controller;


import com.tianji.remark.model.dto.LikeRecordFormDTO;
import com.tianji.remark.service.LikedRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
* @author 29515
* @description 针对表【liked_record(点赞记录表)】的数据库操作Service实现
* @createDate 2024-06-12 15:19:35
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
@Api(tags = "点赞业务相关接口")
public class LikedRecordController{
    private final LikedRecordService likedRecordService;

    @PostMapping
    @ApiOperation("点赞或取消点赞")
    public void addLikeRecord(@Valid @RequestBody LikeRecordFormDTO recordDTO) {
        likedRecordService.addLikeRecord(recordDTO);
    }

    @ApiOperation("点赞或取消点赞")
    @GetMapping("list")
    public Set<Long> getLikesStatusByBizIds(@RequestParam("bizIds") List<Long> bizIds) {
        return likedRecordService.getLikesStatusByBizIds(bizIds);
    }
}