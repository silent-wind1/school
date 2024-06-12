package com.tianji.api.dto.remark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 叶枫
 * @Date: 2024/06/12/15:54
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class LikedTimesDTO {
    /**
     * 点赞的业务id
     */
    private Long bizId;
    /**
     * 总的点赞次数
     */
    private Integer likedTimes;
}