package com.tianji.learning.controller;

import com.tianji.learning.model.query.PointsBoardQuery;
import com.tianji.learning.model.vo.PointsBoardVO;
import com.tianji.learning.service.PointsBoardService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 叶枫
 * @Date: 2024/06/13/13:31
 * @Description:
 */
@Api(tags = "排行磅相关接口")
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class PointsBoardController {
    private final PointsBoardService pointsBoardService;

    public PointsBoardVO queryPointsBoardList(PointsBoardQuery query) {
        return pointsBoardService.queryPointsBoardList(query);
    }
}
