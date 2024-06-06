package com.tianji.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.api.client.course.CatalogueClient;
import com.tianji.api.client.course.CourseClient;
import com.tianji.api.dto.course.CataSimpleInfoDTO;
import com.tianji.api.dto.course.CourseFullInfoDTO;
import com.tianji.api.dto.course.CourseSimpleInfoDTO;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.domain.query.PageQuery;
import com.tianji.common.exceptions.BadRequestException;
import com.tianji.common.exceptions.BizIllegalException;
import com.tianji.common.utils.*;
import com.tianji.learning.mapper.LearningLessonMapper;
import com.tianji.learning.mapper.LearningRecordMapper;
import com.tianji.learning.model.LearningLesson;
import com.tianji.learning.model.LearningRecord;
import com.tianji.learning.model.enums.LessonStatus;
import com.tianji.learning.model.enums.PlanStatus;
import com.tianji.learning.model.vo.LearningLessonVO;
import com.tianji.learning.model.vo.LearningPlanPageVO;
import com.tianji.learning.model.vo.LearningPlanVO;
import com.tianji.learning.service.LearningLessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 叶枫
 * @description 针对表【learning_lesson(学生课程表)】的数据库操作Service实现
 * @createDate 2024-06-04 14:27:56
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LearningLessonServiceImpl extends ServiceImpl<LearningLessonMapper, LearningLesson> implements LearningLessonService {
    private final CourseClient courseClient;

    private final CatalogueClient catalogueClient;

    private final LearningRecordMapper learningRecordMapper;

    @Override
    public void addUserLesson(Long userId, List<Long> courseIds) {
        List<CourseSimpleInfoDTO> simpleInfoList = courseClient.getSimpleInfoList(courseIds);
        if (CollUtils.isEmpty(simpleInfoList)) {
            log.error("课程信息有误， 无法加入到课程中");
            return;
        }
        //         // 2.循环遍历，处理LearningLesson数据
        //        List<LearningLesson> list = new ArrayList<>(cInfoList.size());
        //        for (CourseSimpleInfoDTO cInfo : cInfoList) {
        //            LearningLesson lesson = new LearningLesson();
        //            // 2.1.获取过期时间
        //            Integer validDuration = cInfo.getValidDuration();
        //            if (validDuration != null && validDuration > 0) {
        //                LocalDateTime now = LocalDateTime.now();
        //                lesson.setCreateTime(now);
        //                lesson.setExpireTime(now.plusMonths(validDuration));
        //            }
        //            // 2.2.填充userId和courseId
        //            lesson.setUserId(userId);
        //            lesson.setCourseId(cInfo.getId());
        //            list.add(lesson);
        //        }
        //帮这段循环改成steam流
        List<LearningLesson> lessonList = simpleInfoList.stream().map(info -> {
            LearningLesson learningLesson = new LearningLesson();
            Integer validDuration = info.getValidDuration();
            if (validDuration != null && validDuration > 0) {
                LocalDateTime now = LocalDateTime.now();
                learningLesson.setCreateTime(now);
                learningLesson.setExpireTime(now.plusMinutes(validDuration));
            }
            learningLesson.setUserId(userId);
            learningLesson.setCourseId(info.getId());
            return learningLesson;
        }).collect(Collectors.toList());

        saveBatch(lessonList);
    }

    @Override
    public PageDTO<LearningLessonVO> queryMyLessons(PageQuery query) {
        // 1.获取当前登录用户
        Long userId = UserContext.getUser();
        // 2.分页查询
        // select * from learning_lesson where user_id = #{userId} order by latest_learn_time limit 0, 5
        Page<LearningLesson> page = lambdaQuery()
                .eq(LearningLesson::getUserId, userId) // where user_id = #{userId}
                .page(query.toMpPage("latest_learn_time", false));
        List<LearningLesson> records = page.getRecords();
        if (CollUtils.isEmpty(records)) {
            return PageDTO.empty(page);
        }
        // 3.查询课程信息
        Map<Long, CourseSimpleInfoDTO> cMap = queryCourseSimpleInfoList(records);
        //  List<LearningLessonVO> list = new ArrayList<>(records.size());
        //        // 4.1.循环遍历，把LearningLesson转为VO
        //        for (LearningLesson r : records) {
        //            // 4.2.拷贝基础属性到vo
        //            LearningLessonVO vo = BeanUtils.copyBean(r, LearningLessonVO.class);
        //            // 4.3.获取课程信息，填充到vo
        //            CourseSimpleInfoDTO cInfo = cMap.get(r.getCourseId());
        //            vo.setCourseName(cInfo.getName());
        //            vo.setCourseCoverUrl(cInfo.getCoverUrl());
        //            vo.setSections(cInfo.getSectionNum());
        //            list.add(vo);
        //        }

        // 4.封装VO返回
        List<LearningLessonVO> list = records.stream().map(item -> {
            // 4.2.拷贝基础属性到vo
            LearningLessonVO vo = BeanUtils.copyBean(item, LearningLessonVO.class);
            CourseSimpleInfoDTO cInfo = cMap.get(item.getCourseId());
            vo.setCourseName(cInfo.getName());
            vo.setCourseCoverUrl(cInfo.getCoverUrl());
            vo.setSections(cInfo.getSectionNum());
            return vo;
        }).collect(Collectors.toList());

        return PageDTO.of(page, list);
    }

    @Override
    public LearningLessonVO queryMyCurrentLesson() {
        // 1.获取当前登录的用户
        Long userId = UserContext.getUser();
        // 2.查询正在学习的课程 select * from xx where user_id = #{userId} AND status = 1 order by latest_learn_time limit 1
        LearningLesson lesson = lambdaQuery()
                .eq(LearningLesson::getUserId, userId)
                .eq(LearningLesson::getStatus, LessonStatus.LEARNING.getValue())
                .orderByDesc(LearningLesson::getLatestLearnTime)
                .last("limit 1")
                .one();
        if (lesson == null) {
            return null;
        }
        // 3.拷贝PO基础属性到VO
        LearningLessonVO vo = BeanUtils.copyBean(lesson, LearningLessonVO.class);
        // 4.查询课程信息
        CourseFullInfoDTO cInfo = courseClient.getCourseInfoById(lesson.getCourseId(), false, false);
        if (cInfo == null) {
            throw new BadRequestException("课程不存在");
        }
        vo.setCourseName(cInfo.getName());
        vo.setCourseCoverUrl(cInfo.getCoverUrl());
        vo.setSections(cInfo.getSectionNum());
        // 5.统计课表中的课程数量 select count(1) from xxx where user_id = #{userId}
        Integer courseAmount = lambdaQuery()
                .eq(LearningLesson::getUserId, userId)
                .count();
        vo.setCourseAmount(courseAmount);
        // 6.查询小节信息
        List<CataSimpleInfoDTO> cataInfos =
                catalogueClient.batchQueryCatalogue(CollUtils.singletonList(lesson.getLatestSectionId()));
        if (!CollUtils.isEmpty(cataInfos)) {
            CataSimpleInfoDTO cataInfo = cataInfos.get(0);
            vo.setLatestSectionName(cataInfo.getName());
            vo.setLatestSectionIndex(cataInfo.getCIndex());
        }
        return vo;
    }

    @Override
    public Long isLessonValid(Long courseId) {
        // 1.获取当前登录的用户
        Long userId = UserContext.getUser();

        LearningLesson lesson = lambdaQuery()
                .eq(LearningLesson::getUserId, userId)
                .eq(LearningLesson::getCourseId, courseId)
                .one();
        LocalDateTime expireTime = lesson.getExpireTime();
        if (lesson == null || expireTime == null || LocalDateTime.now().isAfter(expireTime)) {
            log.info("当前用户没有这门课程 lesson = {} ", lesson);
            return null;
        }

        return lesson.getId();
    }

    @Override
    public LearningLessonVO queryLessonByCourseId(Long courseId) {
        // 1.获取当前登录的用户
        Long userId = UserContext.getUser();

        LearningLesson lesson = lambdaQuery()
                .eq(LearningLesson::getUserId, userId)
                .eq(LearningLesson::getCourseId, courseId)
                .one();
        if (lesson == null) {
            return null;
        }

        return BeanUtils.copyBean(lesson, LearningLessonVO.class);
    }

    @Override
    public Integer countLearningLessonByCourse(Long courseId) {
        return lambdaQuery()
                .eq(LearningLesson::getCourseId, courseId)
                .in(LearningLesson::getStatus,
                        LessonStatus.NOT_BEGIN.getValue(),
                        LessonStatus.LEARNING.getValue(),
                        LessonStatus.FINISHED.getValue())
                .count();
    }

    @Override
    public LearningLesson queryByUserAndCourseId(Long userId, Long courseId) {
        return getOne(buildUserIdAndCourseIdWrapper(userId, courseId));
    }

    @Override
    public void createLearningPlan(Long courseId, Integer freq) {
        // 1.获取当前登录的用户
        Long userId = UserContext.getUser();
        // 2.查询课表中的指定课程有关的数据
        LearningLesson lesson = queryByUserAndCourseId(userId, courseId);
        AssertUtils.isNotNull(lesson, "课程信息不存在！");
        // 3.修改数据
        LearningLesson l = new LearningLesson();
        l.setId(lesson.getId());
        l.setWeekFreq(freq);
        if (lesson.getPlanStatus() == PlanStatus.NO_PLAN) {
            l.setPlanStatus(PlanStatus.PLAN_RUNNING);
        }
        updateById(l);
    }

    @Override
    public LearningPlanPageVO queryMyPlans(PageQuery query) {
        LearningPlanPageVO result = new LearningPlanPageVO();
        // 1.获取当前登录用户
        Long userId = UserContext.getUser();
        // 2.获取本周起始时间
        LocalDate now = LocalDate.now();
        LocalDateTime begin = DateUtils.getWeekBeginTime(now);
        LocalDateTime end = DateUtils.getWeekEndTime(now);
        // 3.查询总的统计数据
        // 3.1.本周总的已学习小节数量
        QueryWrapper<LearningLesson> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("sum(week_freq) as plantsTotal");
        queryWrapper.eq("user_id", userId);
        queryWrapper.in("status", LessonStatus.NOT_BEGIN, LessonStatus.LEARNING);
        queryWrapper.eq("plan_status", PlanStatus.PLAN_RUNNING);
        Map<String, Object> map = this.getMap(queryWrapper);
        Integer plantsTotal = 0;
        if (map != null && map.get("plantsTotal") != null) {
            plantsTotal = Integer.valueOf(map.get("plantsTotal").toString());
        }
        // 4. 查询本周 实际 已学习的小姐总数据
        Integer weekFinishedPlanNum = learningRecordMapper.selectCount(Wrappers.<LearningRecord>lambdaQuery()
                .eq(LearningRecord::getUserId, userId)
                .eq(LearningRecord::getFinished, true)
                .between(LearningRecord::getFinishTime, begin, end));
        // 5. 查询课表数据条件
        Page<LearningLesson> page = this.lambdaQuery()
                .eq(LearningLesson::getUserId, userId)
                .in(LearningLesson::getStatus, LessonStatus.NOT_BEGIN, LessonStatus.LEARNING)
                .eq(LearningLesson::getPlanStatus, PlanStatus.PLAN_RUNNING)
                .page(query.toMpPage("latest_learn_time", false));
        List<LearningLesson> records = page.getRecords();
        if (CollUtils.isEmpty(records)) {
            LearningPlanPageVO vo = new LearningPlanPageVO();
            vo.setTotal(0L);
            vo.setPages(0L);
            vo.setList(CollUtils.emptyList());
            return vo;
        }
        // 远程调用
        Set<Long> courseIds = records.stream().map(LearningLesson::getCourseId).collect(Collectors.toSet());
        List<CourseSimpleInfoDTO> simpleInfoList = courseClient.getSimpleInfoList(courseIds);
        if (CollUtils.isEmpty(simpleInfoList)) {
            throw new BizIllegalException("课程不存在");
        }
        Map<Long, CourseSimpleInfoDTO> simpleInfoMap = simpleInfoList.stream().collect(Collectors.toMap(CourseSimpleInfoDTO::getId, c -> c));

        // 查询学习记录表本周已经学习的课程小节
        QueryWrapper<LearningRecord> wrapper = new QueryWrapper<>();
        wrapper.select("lesson_id as lessonId", "count(*) as countId");
        wrapper.eq("user_id", userId);
        wrapper.eq("finished", true);
        wrapper.between("finish_time", begin, end);
        wrapper.groupBy("lesson_id");
        List<LearningRecord> learningRecords = learningRecordMapper.selectList(wrapper);
        Map<Long, Long> courseWeekFinish = learningRecords.stream().collect(Collectors.toMap(LearningRecord::getLessonId, c -> c.getUserId()));
        // 封装vo
        LearningPlanPageVO learningPlanPageVO = new LearningPlanPageVO();
        learningPlanPageVO.setWeekTotalPlan(plantsTotal);
        learningPlanPageVO.setWeekFinished(weekFinishedPlanNum);
        List<LearningPlanVO> PlanVoList = records.stream().map(record -> {
            LearningPlanVO learningPlanVO = BeanUtils.copyBean(record, LearningPlanVO.class);
            CourseSimpleInfoDTO InfoDTO = simpleInfoMap.get(record.getCourseId());
            if (InfoDTO != null) {
                learningPlanVO.setCourseName(InfoDTO.getName());
                learningPlanVO.setSections(InfoDTO.getSectionNum());
            }
            learningPlanVO.setWeekLearnedSections(courseWeekFinish.getOrDefault(record.getId(), 0L).intValue());
            return learningPlanVO;
        }).collect(Collectors.toList());
        return learningPlanPageVO.pageInfo(page.getTotal(), page.getPages(), PlanVoList);
    }

    private Map<Long, CourseSimpleInfoDTO> queryCourseSimpleInfoList(List<LearningLesson> records) {
        // 3.1.获取课程id
        Set<Long> cIds = records.stream().map(LearningLesson::getCourseId).collect(Collectors.toSet());
        // 3.2.查询课程信息
        List<CourseSimpleInfoDTO> cInfoList = courseClient.getSimpleInfoList(cIds);
        if (CollUtils.isEmpty(cInfoList)) {
            // 课程不存在，无法添加
            throw new BadRequestException("课程信息不存在！");
        }
        // 3.3.把课程集合处理成Map，key是courseId，值是course本身
        Map<Long, CourseSimpleInfoDTO> cMap = cInfoList.stream()
                .collect(Collectors.toMap(CourseSimpleInfoDTO::getId, c -> c));
        return cMap;
    }

    private LambdaQueryWrapper<LearningLesson> buildUserIdAndCourseIdWrapper(Long userId, Long courseId) {
        LambdaQueryWrapper<LearningLesson> queryWrapper = new QueryWrapper<LearningLesson>()
                .lambda()
                .eq(LearningLesson::getUserId, userId)
                .eq(LearningLesson::getCourseId, courseId);
        return queryWrapper;
    }
}




