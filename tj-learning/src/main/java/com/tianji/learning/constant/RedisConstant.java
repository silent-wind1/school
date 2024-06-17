package com.tianji.learning.constant;

/**
 * @Author: 叶枫
 * @Date: 2024/06/13/18:00
 * @Description:
 */
public interface RedisConstant {
    /**
     * 签到记录的Key的前缀
     */
    String SIGN_RECODE_KEY_PREFIX = "sign:uid:";
    /**
     * 积分排行榜的Key前缀
     */
    String POINTS_BOARD_KEY_PREFIX = "boards:";
}
