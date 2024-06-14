package com.tianji.learning.controller;

import com.tianji.learning.model.vo.PointsStatisticsVO;
import com.tianji.learning.service.PointsRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: 叶枫
 * @Date: 2024/06/14/14:16
 * @Description:
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/points")
@Api(tags = "积分相关接口")
public class PointsRecordController {
    private final PointsRecordService pointsRecordService;

    @ApiOperation("查询我的今日积分")
    @GetMapping("today")
    public List<PointsStatisticsVO> queryMyPointsToday(){
        return pointsRecordService.queryMyPointsToday();
    }
}
