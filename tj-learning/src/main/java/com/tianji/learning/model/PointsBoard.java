package com.tianji.learning.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 学霸天梯榜
 * @TableName points_board
 */
@TableName(value ="points_board")
@Data
public class PointsBoard implements Serializable {
    /**
     * 榜单id
     */
    @TableId
    private Long id;

    /**
     * 学生id
     */
    private Long userId;

    /**
     * 积分值
     */
    private Integer points;

    /**
     * 名次，只记录赛季前100
     */
    private Integer rank;

    /**
     * 赛季，例如 1,就是第一赛季，2-就是第二赛季
     */
    private Integer season;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}