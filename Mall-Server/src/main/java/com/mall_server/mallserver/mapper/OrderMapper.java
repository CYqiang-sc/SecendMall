package com.mall_server.mallserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall_server.mallserver.entity.Order;

import java.util.List;
import java.util.Map;

/**
 * 订单Mapper接口
 */
public interface OrderMapper extends BaseMapper<Order> {
    List<Order> list(Map<String,Object> map);

    Long getTotal(Map<String,Object> map);
}
