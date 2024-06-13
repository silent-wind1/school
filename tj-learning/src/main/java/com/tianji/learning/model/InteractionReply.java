package com.tianji.learning.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 互动问题的回答或评论
 * @TableName interaction_reply
 */
@TableName(value ="interaction_reply")
@Data
public class InteractionReply implements Serializable {
    /**
     * 互动问题的回答id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 互动问题问题id
     */
    private Long questionId;

    /**
     * 回复的上级回答id
     */
    private Long answerId;

    /**
     * 回答者id
     */
    private Long userId;

    /**
     * 回答内容
     */
    private String content;

    /**
     * 回复的目标用户id
     */
    private Long targetUserId;

    /**
     * 回复的目标回复id
     */
    private Long targetReplyId;

    /**
     * 评论数量
     */
    private Integer replyTimes;

    /**
     * 点赞数量
     */
    private Integer likedTimes;

    /**
     * 是否被隐藏，默认false
     */
    private Boolean hidden;

    /**
     * 是否匿名，默认false
     */
    private Boolean anonymity;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}