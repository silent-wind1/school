package com.tianji.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.common.utils.DateUtils;
import com.tianji.learning.mapper.PointsRecordMapper;
import com.tianji.learning.model.PointsRecord;
import com.tianji.learning.model.enums.PointsRecordType;
import com.tianji.learning.mq.msg.SignInMessage;
import com.tianji.learning.service.PointsRecordService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static cn.hutool.poi.excel.sax.AttributeName.r;

/**
* @author 29515
* @description 针对表【points_record(学习积分记录，每个月底清零)】的数据库操作Service实现
* @createDate 2024-06-12 20:50:29
*/
@Service
public class PointsRecordServiceImpl extends ServiceImpl<PointsRecordMapper, PointsRecord>
    implements PointsRecordService{

    @Override
    public void addPointRecord(SignInMessage message, PointsRecordType type) {
        if (message.getUserId() == null || message.getPoints() == null) {
            return;
        }
        Integer realPoint = message.getPoints();
        int maxPoints = type.getMaxPoints();
        if (maxPoints > 0) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime dayStartTime = DateUtils.getDayStartTime(now);
            LocalDateTime dayEndTime = DateUtils.getDayEndTime(now);
            QueryWrapper<PointsRecord> wrapper = new QueryWrapper<>();
            wrapper.select("sum(points) as totalPoints");
            wrapper.eq("user_id", message.getUserId());
            wrapper.eq("type", type);
            wrapper.between("create_time", dayStartTime, dayEndTime);
            Map<String, Object> map = this.getMap(wrapper);
            int currentPoints = 0;
            if (map != null) {
                BigDecimal totalPoints = (BigDecimal) map.get("totalPoints");
                currentPoints = totalPoints.intValue();
            }

            if (currentPoints > maxPoints) {
                return;
            }

            if (currentPoints + realPoint  > maxPoints) {
                realPoint = maxPoints - currentPoints;
            }

            PointsRecord pointsRecord = new PointsRecord();
            pointsRecord.setUserId(message.getUserId());
            pointsRecord.setPoints(realPoint);
            pointsRecord.setType(type);
            this.save(pointsRecord);
        }
    }
}




