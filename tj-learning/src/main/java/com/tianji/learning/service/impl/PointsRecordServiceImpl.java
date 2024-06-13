package com.tianji.learning.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.learning.mapper.PointsRecordMapper;
import com.tianji.learning.model.PointsRecord;
import com.tianji.learning.service.PointsRecordService;
import org.springframework.stereotype.Service;

/**
* @author 29515
* @description 针对表【points_record(学习积分记录，每个月底清零)】的数据库操作Service实现
* @createDate 2024-06-12 20:50:29
*/
@Service
public class PointsRecordServiceImpl extends ServiceImpl<PointsRecordMapper, PointsRecord>
    implements PointsRecordService{

}




