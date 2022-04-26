package com.xiaomou.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaomou.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaomou.vo.MessageVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
public interface MessageService extends IService<Message> {
    /**
     * 添加留言弹幕
     *
     * @param messageVO 留言对象
     */
    void saveMessage(MessageVO messageVO);

    /**查看留言列表
     *
     * @param current
     * @param size
     * @param nickname
     * @return
     */
    IPage<Message> getMessageList(Integer current, Integer size, String nickname) ;
}
