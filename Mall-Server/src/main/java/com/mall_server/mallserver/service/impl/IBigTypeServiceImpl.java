package com.mall_server.mallserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall_server.mallserver.entity.BigType;
import com.mall_server.mallserver.mapper.BigTypeMapper;
import com.mall_server.mallserver.service.IBigTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商品大类Service实现类
 */
@Service("bigTypeService")
public class IBigTypeServiceImpl extends ServiceImpl<BigTypeMapper,BigType> implements IBigTypeService {

    @Autowired
    private BigTypeMapper bigTypeMapper;
}
