package com.xiaomou.service.impl;

import com.xiaomou.entity.About;
import com.xiaomou.mapper.AboutMapper;
import com.xiaomou.service.AboutService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
@Service
public class AboutServiceImpl extends ServiceImpl<AboutMapper, About> implements AboutService {

}
