package com.tianji.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianji.message.domain.po.NoticeTemplate;
import com.tianji.message.domain.po.PublicNotice;

/**
 * <p>
 * 公告消息模板 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-19
 */
public interface IPublicNoticeService extends IService<PublicNotice> {

    void saveNoticeOfTemplate(NoticeTemplate noticeTemplate);
}
