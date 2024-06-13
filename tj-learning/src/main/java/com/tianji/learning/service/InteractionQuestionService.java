package com.tianji.learning.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.learning.model.InteractionQuestion;
import com.tianji.learning.model.dto.QuestionFormDTO;
import com.tianji.learning.model.query.QuestionAdminPageQuery;
import com.tianji.learning.model.query.QuestionPageQuery;
import com.tianji.learning.model.vo.QuestionAdminVO;
import com.tianji.learning.model.vo.QuestionVO;

/**
* @author 29515
* @description 针对表【interaction_question(互动提问的问题表)】的数据库操作Service
* @createDate 2024-06-11 12:32:47
*/
public interface InteractionQuestionService extends IService<InteractionQuestion> {
    void saveQuestion(QuestionFormDTO questionDTO);

    PageDTO<QuestionVO> queryQuestionPage(QuestionPageQuery query);

    QuestionVO queryQuestionById(Long id);

    PageDTO<QuestionAdminVO> queryQuestionPageAdmin(QuestionAdminPageQuery query);

    QuestionAdminVO queryQuestionByIdAdmin(Long id);

    void hiddenQuestion(Long id, Boolean hidden);

    void updateQuestion(Long id, QuestionFormDTO questionDTO);

    void deleteById(Long id);
}
