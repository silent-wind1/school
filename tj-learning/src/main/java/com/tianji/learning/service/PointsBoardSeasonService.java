package com.tianji.learning.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianji.learning.model.PointsBoardSeason;

import java.time.LocalTime;

/**
* @author 29515
* @description 针对表【points_board_season】的数据库操作Service
* @createDate 2024-06-12 20:50:29
*/
public interface PointsBoardSeasonService extends IService<PointsBoardSeason> {
    Integer querySeasonByTime(LocalTime time);
    void createPointsBoardTableBySeason(Integer season);
}
