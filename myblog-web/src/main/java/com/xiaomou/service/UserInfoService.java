package com.xiaomou.service;

import com.xiaomou.dto.BlogHomeInfoDTO;
import com.xiaomou.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
public interface UserInfoService extends IService<UserInfo> {
    BlogHomeInfoDTO getBlogInfo();
}
