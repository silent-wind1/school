package com.tianji.learning.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.learning.mapper.PointsBoardSeasonMapper;
import com.tianji.learning.model.PointsBoardSeason;
import com.tianji.learning.service.PointsBoardSeasonService;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;

import static com.tianji.learning.constant.LearningConstant.POINTS_BOARD_KEY_PREFIX;

/**
 * @author 29515
 * @description 针对表【points_board_season】的数据库操作Service实现
 * @createDate 2024-06-12 20:50:29
 */
@Service
public class PointsBoardSeasonServiceImpl extends ServiceImpl<PointsBoardSeasonMapper, PointsBoardSeason> implements PointsBoardSeasonService {

    @Override
    public Integer querySeasonByTime(LocalTime time) {
        Optional<PointsBoardSeason> optional = lambdaQuery()
                .le(PointsBoardSeason::getBeginTime, time)
                .ge(PointsBoardSeason::getEndTime, time)
                .oneOpt();
        return optional.map(PointsBoardSeason::getId).orElse(null);
    }

    @Override
    public void createPointsBoardTableBySeason(Integer season) {
        getBaseMapper().createPointsBoardTable(POINTS_BOARD_KEY_PREFIX + season);
    }
}




