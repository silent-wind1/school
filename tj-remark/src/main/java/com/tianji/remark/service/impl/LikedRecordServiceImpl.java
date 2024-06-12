package com.tianji.remark.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.api.client.remark.RemarkClient;
import com.tianji.api.dto.remark.LikedTimesDTO;
import com.tianji.common.autoconfigure.mq.RabbitMqHelper;
import com.tianji.common.utils.CollUtils;
import com.tianji.common.utils.StringUtils;
import com.tianji.common.utils.UserContext;
import com.tianji.remark.constants.RedisConstants;
import com.tianji.remark.mapper.LikedRecordMapper;
import com.tianji.remark.model.LikedRecord;
import com.tianji.remark.model.dto.LikeRecordFormDTO;
import com.tianji.remark.service.LikedRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.tianji.common.constants.MqConstants.Exchange.LIKE_RECORD_EXCHANGE;
import static com.tianji.common.constants.MqConstants.Key.LIKED_TIMES_KEY_TEMPLATE;

/**
 * @author 29515
 * @description 针对表【liked_record(点赞记录表)】的数据库操作Service实现
 * @createDate 2024-06-12 15:19:35
 */
@Service
@RequiredArgsConstructor
public class LikedRecordServiceImpl extends ServiceImpl<LikedRecordMapper, LikedRecord> implements LikedRecordService {

    private final RabbitMqHelper mqHelper;
    private final StringRedisTemplate redisTemplate;
    private final RemarkClient remarkClient;

    @Override
    public void addLikeRecord(LikeRecordFormDTO recordDTO) {
        boolean success = recordDTO.getLiked() ? like(recordDTO) : unlike(recordDTO);
        if (!success) {
            return;
        }
        Long likeTimes = redisTemplate.opsForSet().size(RedisConstants.LIKE_BIZ_KEY_PREFIX + recordDTO.getBizId());
        if (likeTimes == null) {
            return;
        }
        // 缓存点赞总数到Redis中
        redisTemplate.opsForZSet().add(
                RedisConstants.LIKE_BIZ_KEY_PREFIX + recordDTO.getBizType(),
                recordDTO.getBizType(),
                likeTimes);
    }

    @Override
    public Set<Long> getLikesStatusByBizIds(List<Long> bizIds) {
        if (CollUtils.isEmpty(bizIds)) {
            return CollUtils.emptySet();
        }
        Long userId = UserContext.getUser();
        List<LikedRecord> list = this.lambdaQuery().in(LikedRecord::getBizId, bizIds)
                .eq(LikedRecord::getUserId, userId).list();
        return list.stream().map(LikedRecord::getBizId).collect(Collectors.toSet());
    }

    @Override
    public void readLikedTimesAndSendMessage(String bizType, int maxBizSize) {
        String key = RedisConstants.LIKE_BIZ_KEY_PREFIX + bizType;
        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet().popMin(key, maxBizSize);
        if (CollUtils.isEmpty(tuples)) {
            return;
        }
        List<LikedTimesDTO> list = new ArrayList<>(tuples.size());
        for (ZSetOperations.TypedTuple<String> tuple : tuples) {
            String bizId = tuple.getValue();
            Double likedTimes = tuple.getScore();
            if (bizId == null || likedTimes == null) {
                continue;
            }
            list.add(LikedTimesDTO.of(Long.valueOf(bizId), likedTimes.intValue()));
        }
        // 3.发送MQ消息
        mqHelper.send(
                LIKE_RECORD_EXCHANGE,
                StringUtils.format(LIKED_TIMES_KEY_TEMPLATE, bizType),
                list);
    }

    private Boolean like(LikeRecordFormDTO recordDTO) {
        Long userId = UserContext.getUser();
        String key = RedisConstants.LIKE_BIZ_KEY_PREFIX + recordDTO.getBizId();
        Long result = redisTemplate.opsForSet().add(key, userId.toString());
        return result != null && result > 0;
    }


    private Boolean unlike(LikeRecordFormDTO recordDTO) {
        Long userId = UserContext.getUser();
        String key = RedisConstants.LIKE_BIZ_KEY_PREFIX + recordDTO.getBizId();
        Long result = redisTemplate.opsForSet().remove(key, userId.toString());
        return result != null && result > 0;
    }
}




