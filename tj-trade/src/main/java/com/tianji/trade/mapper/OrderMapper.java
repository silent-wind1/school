package com.tianji.trade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianji.trade.domain.po.Order;

/**
 * <p>
 * 订单 Mapper 接口
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-29
 */
public interface OrderMapper extends BaseMapper<Order> {

    Order getById(Long id);
}
