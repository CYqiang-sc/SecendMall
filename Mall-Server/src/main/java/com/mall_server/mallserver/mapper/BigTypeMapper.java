package com.mall_server.mallserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall_server.mallserver.entity.BigType;

/**
 * 商品大类Mapper接口
 */
public interface BigTypeMapper extends BaseMapper<BigType> {
    BigType findById(Integer id);
}
