package com.tianji.learning.service.impl;

import com.tianji.common.autoconfigure.mq.RabbitMqHelper;
import com.tianji.common.constants.MqConstants;
import com.tianji.common.exceptions.BizIllegalException;
import com.tianji.common.utils.CollUtils;
import com.tianji.common.utils.UserContext;
import com.tianji.learning.constant.RedisConstant;
import com.tianji.learning.model.vo.SignResultVO;
import com.tianji.learning.mq.msg.SignInMessage;
import com.tianji.learning.service.SignRecordsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 * @Author: 叶枫
 * @Date: 2024/06/13/16:31
 * @Description:
 */
@Service
@RequiredArgsConstructor
public class SignRecordsServiceImpl implements SignRecordsService {
    private final StringRedisTemplate redisTemplate;
    private final RabbitMqHelper rabbitMqHelper;

    @Override
    public SignResultVO addSignRecords() {
        Long userId = UserContext.getUser();
        LocalDate now = LocalDate.now();
        String date = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = RedisConstant.SIGN_RECODE_KEY_PREFIX + userId.toString() + date;
        long offset = now.getDayOfMonth() - 1;
        Boolean result = redisTemplate.opsForValue().setBit(key, offset, true);
        if (Boolean.TRUE.equals(result)) {
            throw new BizIllegalException("不能重复签到");
        }
        int days = countSignDays(key, now.getDayOfMonth());
        int rewardPoints = 0;
        switch (days) {
            case 7: rewardPoints = 10;break;
            case 14: rewardPoints = 20; break;
            case 28: rewardPoints = 40; break;
        }
        // 保存积分
        rabbitMqHelper.send(MqConstants.Exchange.LEARNING_EXCHANGE,
                MqConstants.Key.SIGN_IN,
                SignInMessage.of(userId, rewardPoints + 1));
        SignResultVO vo = new SignResultVO();
        vo.setSignDays(days);
        vo.setRewardPoints(rewardPoints);
        return vo;
    }

    private int countSignDays(String key, int dayOfMonth) {
        List<Long> field = redisTemplate.opsForValue().bitField(key, BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth)).valueAt(0));
        if (CollUtils.isEmpty(field)) {
            return 0;
        }
        Long num = field.get(0);
        int count = 0;
        while ((num & 1) == 1) {
            count++;
            num = num >>> 1;
        }
        return count;
    }
}
