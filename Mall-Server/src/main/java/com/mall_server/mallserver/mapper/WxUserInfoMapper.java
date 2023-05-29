package com.mall_server.mallserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall_server.mallserver.entity.BigType;
import com.mall_server.mallserver.entity.WxUserInfo;

/**
 * 微信用户Mapper接口
 */
public interface WxUserInfoMapper extends BaseMapper<WxUserInfo> {
    WxUserInfo findByOpenId(Integer id);
}
