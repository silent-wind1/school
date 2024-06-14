package com.tianji.learning.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianji.learning.model.PointsRecord;
import com.tianji.learning.model.enums.PointsRecordType;
import com.tianji.learning.mq.msg.SignInMessage;

/**
* @author 29515
* @description 针对表【points_record(学习积分记录，每个月底清零)】的数据库操作Service
* @createDate 2024-06-12 20:50:29
*/
public interface PointsRecordService extends IService<PointsRecord> {

    void addPointRecord(SignInMessage message, PointsRecordType type);
}
