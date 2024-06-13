package com.tianji.learning.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName points_board_season
 */
@TableName(value ="points_board_season")
@Data
public class PointsBoardSeason implements Serializable {
    /**
     * 自增长id，season标示
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 赛季名称，例如：第1赛季
     */
    private String name;

    /**
     * 赛季开始时间
     */
    private Date beginTime;

    /**
     * 赛季结束时间
     */
    private Date endTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}