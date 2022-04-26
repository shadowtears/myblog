package com.xiaomou.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaomou.entity.FriendLink;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
public interface FriendLinkService extends IService<FriendLink> {

    boolean  addOrEditFriendLink(FriendLink friendLink) ;

    IPage<FriendLink> pageLinks(Integer current,Integer size,String keywords);
}
