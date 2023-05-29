package com.mall_server.mallserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall_server.mallserver.entity.Audit;
import com.mall_server.mallserver.entity.Product;
import org.springframework.transaction.annotation.Transactional;


/**
 * 商品Service接口
 */
@Transactional
public interface IAuditService extends IService<Audit> {

}
