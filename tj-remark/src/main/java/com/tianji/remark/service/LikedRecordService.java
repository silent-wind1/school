package com.tianji.remark.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianji.remark.model.LikedRecord;
import com.tianji.remark.model.dto.LikeRecordFormDTO;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
* @author 29515
* @description 针对表【liked_record(点赞记录表)】的数据库操作Service
* @createDate 2024-06-12 15:19:35
*/
public interface LikedRecordService extends IService<LikedRecord> {

    void addLikeRecord(@Valid LikeRecordFormDTO recordDTO);

    Set<Long> getLikesStatusByBizIds(List<Long> bizIds);

    void readLikedTimesAndSendMessage(String bizType, int maxBizSize);
}
