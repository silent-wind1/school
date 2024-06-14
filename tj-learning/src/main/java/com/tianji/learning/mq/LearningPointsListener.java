package com.tianji.learning.mq;

import com.tianji.common.constants.MqConstants;
import com.tianji.learning.model.enums.PointsRecordType;
import com.tianji.learning.mq.msg.SignInMessage;
import com.tianji.learning.service.PointsRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author: 叶枫
 * @Date: 2024/06/14/13:02
 * @Description:
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LearningPointsListener {
    private final PointsRecordService pointsRecordService;

    /**
     * 签到增加积分
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "sign.points.queue", durable = "true"),
            exchange = @Exchange(value = MqConstants.Exchange.LEARNING_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = MqConstants.Key.SIGN_IN))
    public void listenSignInListener(SignInMessage message) {
        log.debug("消费到消息 签到增加积分 {}" , message);
        pointsRecordService.addPointRecord(message, PointsRecordType.SIGN);
    }

    /**
     * 问答增加积分
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ga.points.queue", durable = "true"),
            exchange = @Exchange(value = MqConstants.Exchange.LEARNING_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = MqConstants.Key.WRITE_REPLY))
    public void listenReplayListener(SignInMessage message) {
        log.debug("消费到消息 问答增加积分 {}" , message);

    }
}
