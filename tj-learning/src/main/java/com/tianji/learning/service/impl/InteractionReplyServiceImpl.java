package com.tianji.learning.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.utils.BeanUtils;
import com.tianji.common.utils.UserContext;
import com.tianji.learning.model.InteractionReply;
import com.tianji.learning.model.dto.ReplyDTO;
import com.tianji.learning.model.query.ReplyPageQuery;
import com.tianji.learning.model.vo.ReplyVO;
import com.tianji.learning.service.InteractionReplyService;
import com.tianji.learning.mapper.InteractionReplyMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 29515
 * @description 针对表【interaction_reply(互动问题的回答或评论)】的数据库操作Service实现
 * @createDate 2024-06-11 12:32:47
 */
@Service
public class InteractionReplyServiceImpl extends ServiceImpl<InteractionReplyMapper, InteractionReply> implements InteractionReplyService {

    @Override
    @Transactional
    public void saveReply(ReplyDTO replyDTO) {
    }

    @Override
    public PageDTO<ReplyVO> queryReplyPage(ReplyPageQuery pageQuery, boolean isStudent) {
        return null;
    }

    @Override
    public void hiddenReply(Long id, Boolean hidden) {

    }

    @Override
    public ReplyVO queryReplyById(Long id) {
        return null;
    }
}




