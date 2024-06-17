package com.tianji.learning.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianji.learning.model.PointsBoard;
import com.tianji.learning.model.query.PointsBoardQuery;
import com.tianji.learning.model.vo.PointsBoardVO;

/**
* @author 29515
* @description 针对表【points_board(学霸天梯榜)】的数据库操作Service
* @createDate 2024-06-12 20:50:29
*/
public interface PointsBoardService extends IService<PointsBoard> {

    PointsBoardVO queryPointsBoardList(PointsBoardQuery query);
}
