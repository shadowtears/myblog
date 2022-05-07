package com.xiaomou.service;

import com.xiaomou.dto.BlogHomeInfoDTO;
import com.xiaomou.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaomou.vo.UserInfoVO;
import org.springframework.web.multipart.MultipartFile;

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

    void updateUserInfo(UserInfoVO userInfoVO);

    String updateUserAvatar(MultipartFile file);
}
