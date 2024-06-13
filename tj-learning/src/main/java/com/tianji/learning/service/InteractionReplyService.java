package com.tianji.learning.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.learning.model.InteractionReply;
import com.tianji.learning.model.dto.ReplyDTO;
import com.tianji.learning.model.query.ReplyPageQuery;
import com.tianji.learning.model.vo.ReplyVO;

/**
* @author 29515
* @description 针对表【interaction_reply(互动问题的回答或评论)】的数据库操作Service
* @createDate 2024-06-11 12:32:47
*/
public interface InteractionReplyService extends IService<InteractionReply> {
    void saveReply(ReplyDTO replyDTO);

    PageDTO<ReplyVO> queryReplyPage(ReplyPageQuery pageQuery, boolean isStudent);

    void hiddenReply(Long id, Boolean hidden);

    ReplyVO queryReplyById(Long id);
}
