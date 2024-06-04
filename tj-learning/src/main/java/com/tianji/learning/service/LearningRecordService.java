package com.tianji.learning.service;

import com.tianji.api.dto.leanring.LearningLessonDTO;
import com.tianji.api.dto.leanring.LearningRecordFormDTO;
import com.tianji.learning.model.LearningRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 29515
* @description 针对表【learning_record(学习记录表)】的数据库操作Service
* @createDate 2024-06-04 18:06:44
*/
public interface LearningRecordService extends IService<LearningRecord> {

    LearningLessonDTO queryLearningRecordByCourse(Long courseId);

    void addLearningRecord(LearningRecordFormDTO formDTO);
}
