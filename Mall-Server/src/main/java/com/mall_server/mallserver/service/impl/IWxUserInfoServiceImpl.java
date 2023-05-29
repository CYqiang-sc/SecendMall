package com.mall_server.mallserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall_server.mallserver.entity.WxUserInfo;
import com.mall_server.mallserver.mapper.WxUserInfoMapper;
import com.mall_server.mallserver.service.IWxUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信用户Service实现类
 */
@Service("wxUserInfoService")
public class IWxUserInfoServiceImpl extends ServiceImpl<WxUserInfoMapper,WxUserInfo> implements IWxUserInfoService {

    @Autowired
    private WxUserInfoMapper wxUserInfoMapper;

}
