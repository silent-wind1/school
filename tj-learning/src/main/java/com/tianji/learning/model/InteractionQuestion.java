package com.tianji.learning.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.tianji.learning.model.enums.QuestionStatus;
import lombok.Data;

/**
 * 互动提问的问题表
 * @TableName interaction_question
 */
@TableName(value ="interaction_question")
@Data
public class InteractionQuestion implements Serializable {
    /**
     * 主键，互动问题的id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 互动问题的标题
     */
    private String title;

    /**
     * 问题描述信息
     */
    private String description;

    /**
     * 所属课程id
     */
    private Long courseId;

    /**
     * 所属课程章id
     */
    private Long chapterId;

    /**
     * 所属课程节id
     */
    private Long sectionId;

    /**
     * 提问学员id
     */
    private Long userId;

    /**
     * 最新的一个回答的id
     */
    private Long latestAnswerId;

    /**
     * 问题下的回答数量
     */
    private Integer answerTimes;

    /**
     * 是否匿名，默认false
     */
    private Boolean anonymity;

    /**
     * 是否被隐藏，默认false
     */
    private Boolean hidden;

    /**
     * 管理端问题状态：0-未查看，1-已查看
     */
    private QuestionStatus status;

    /**
     * 提问时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}