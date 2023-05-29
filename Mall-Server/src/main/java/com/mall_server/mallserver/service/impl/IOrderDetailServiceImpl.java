package com.mall_server.mallserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall_server.mallserver.entity.OrderDetail;
import com.mall_server.mallserver.mapper.OrderDetailMapper;
import com.mall_server.mallserver.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单详情Service实现类
 */
@Service("orderDetailService")
public class IOrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper,OrderDetail> implements IOrderDetailService {

    @Autowired
    private OrderDetailMapper orderDetailMapper;
}
