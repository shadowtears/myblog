package com.xiaomou.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomou.entity.Message;
import com.xiaomou.mapper.MessageMapper;
import com.xiaomou.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaomou.util.IpUtil;
import com.xiaomou.vo.MessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private HttpServletRequest request;
    /**
     * 添加留言弹幕
     *
     * @param messageVO 留言对象
     */
    @Override
    public void saveMessage(MessageVO messageVO) {
        String ipAddr = IpUtil.getIp(request);
        String ipSource = IpUtil.getIpSource(ipAddr);
        Message message = new Message();
        message.setAvatar(messageVO.getAvatar());
        message.setNickname(messageVO.getNickname());
        message.setCreateTime(new Date());
        message.setMessageContent(messageVO.getMessageContent());
        message.setIpAddress(ipAddr);
        message.setIpSource(ipSource);
        message.setTime(messageVO.getTime());
        this.baseMapper.insert(message);
    }

    /**
     * 查看留言列表
     *
     * @param current
     * @param size
     * @param nickname
     * @return
     */
    @Override
    public IPage<Message> getMessageList(Integer current, Integer size, String nickname) {

        Page<Message> page=null;
        if(current==null || size ==null){
            current=1;
            size=99999;
        }
        page=new Page<>(current,size);
        QueryWrapper<Message> wrapper=null;
        if(nickname!=null){
            wrapper.like("nickname", nickname);
        }
        return this.baseMapper.selectPage(page,wrapper);

    }
}
