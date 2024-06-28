package com.tianji.learning.controller;

import com.tianji.learning.model.vo.SignResultVO;
import com.tianji.learning.service.SignRecordsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 叶枫
 * @Date: 2024/06/13/16:31
 * @Description:
 */
@Api(tags = "签到相关接口")
@RestController
@RequestMapping("sign-records")
@RequiredArgsConstructor
public class SignRecordsController {

    private final SignRecordsService signRecordsService;

    @ApiOperation("签到")
    @PostMapping
    public SignResultVO addSignRecords() {
        return signRecordsService.addSignRecords();
    }

    @ApiOperation("签到查询记录")
    @GetMapping
    public Byte[] querySignRecords() {
        return signRecordsService.querySignRecords();
    }
}
