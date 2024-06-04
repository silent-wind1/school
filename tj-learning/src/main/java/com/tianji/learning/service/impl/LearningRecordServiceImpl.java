package com.tianji.learning.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.api.dto.leanring.LearningLessonDTO;
import com.tianji.api.dto.leanring.LearningRecordDTO;
import com.tianji.api.dto.leanring.LearningRecordFormDTO;
import com.tianji.common.utils.BeanUtils;
import com.tianji.common.utils.UserContext;
import com.tianji.learning.mapper.LearningLessonMapper;
import com.tianji.learning.model.LearningLesson;
import com.tianji.learning.model.LearningRecord;
import com.tianji.learning.service.LearningRecordService;
import com.tianji.learning.mapper.LearningRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 29515
 * @description 针对表【learning_record(学习记录表)】的数据库操作Service实现
 * @createDate 2024-06-04 18:06:44
 */
@Service
@RequiredArgsConstructor
public class LearningRecordServiceImpl extends ServiceImpl<LearningRecordMapper, LearningRecord> implements LearningRecordService {
    private final LearningLessonMapper learningLessonMapper;

    @Override
    public LearningLessonDTO queryLearningRecordByCourse(Long courseId) {
        Long userId = UserContext.getUser();
        QueryWrapper<LearningLesson> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("course_id", courseId);
        LearningLesson learningLesson = learningLessonMapper.selectOne(queryWrapper);

        List<LearningRecord> records = lambdaQuery().eq(LearningRecord::getLessonId, learningLesson.getId()).list();
        // 4.封装结果
        LearningLessonDTO dto = new LearningLessonDTO();
        dto.setId(learningLesson.getId());
        dto.setLatestSectionId(learningLesson.getLatestSectionId());
        dto.setRecords(BeanUtils.copyList(records, LearningRecordDTO.class));
        return dto;
    }

    @Override
    public void addLearningRecord(LearningRecordFormDTO formDTO) {
        // 1.获取登录用户
        Long userId = UserContext.getUser();
        // 2.处理学习记录

    }
}




