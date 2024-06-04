package com.tianji.learning.mq;

import com.tianji.api.dto.trade.OrderBasicDTO;
import com.tianji.common.constants.MqConstants;
import com.tianji.common.utils.CollUtils;
import com.tianji.learning.service.LearningLessonService;
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
 * @Date: 2024/06/04/14:33
 * @Description:
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class LessonChangeListener {

    private final LearningLessonService lessonService;

    /**
     * @param orderBasicDTO
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "learning.lesson.pay.queue"), exchange = @Exchange(value = MqConstants.Exchange.ORDER_EXCHANGE, type = ExchangeTypes.TOPIC), key = MqConstants.Key.ORDER_PAY_KEY))
    public void OnMsg(OrderBasicDTO orderBasicDTO) {
        log.info("LessonChangeListener 接收到消息 用户={}， 添加了课程 = {}", orderBasicDTO.getUserId(), orderBasicDTO.getCourseIds());
        if (orderBasicDTO.getUserId() == null || orderBasicDTO.getOrderId() == null || CollUtils.isEmpty(orderBasicDTO.getCourseIds())) {
            return;
        }
        lessonService.addUserLesson(orderBasicDTO.getUserId(), orderBasicDTO.getCourseIds());
    }
}
