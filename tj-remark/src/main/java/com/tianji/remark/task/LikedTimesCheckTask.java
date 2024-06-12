package com.tianji.remark.task;

import com.tianji.remark.service.LikedRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: 叶枫
 * @Date: 2024/06/12/18:07
 * @Description:
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class LikedTimesCheckTask {
    private static final List<String> BIZ_TYPES = List.of("QA", "NOTE");
    private static final int MAX_BIZ_SIZE = 30;

    private final LikedRecordService recordService;

    @Scheduled(fixedDelay = 20000)
    public void checkLikedTimes(){
        for (String bizType : BIZ_TYPES) {
            recordService.readLikedTimesAndSendMessage(bizType, MAX_BIZ_SIZE);
        }
    }
}
