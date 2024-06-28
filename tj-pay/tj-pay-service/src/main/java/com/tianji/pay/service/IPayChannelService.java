package com.tianji.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianji.pay.domain.po.PayChannel;
import com.tianji.pay.sdk.dto.PayChannelDTO;

/**
 * <p>
 * 支付渠道 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-26
 */
public interface IPayChannelService extends IService<PayChannel> {

    Long addPayChannel(PayChannelDTO channelDTO);

    void updatePayChannel(PayChannelDTO channelDTO);
}
