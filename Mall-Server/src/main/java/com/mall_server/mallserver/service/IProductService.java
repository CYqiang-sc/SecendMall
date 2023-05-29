package com.mall_server.mallserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall_server.mallserver.entity.Product;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 商品Service接口
 */
@Transactional
public interface IProductService extends IService<Product> {
    List<Product> list(Map<String,Object> map);

    Long getTotal(Map<String,Object> map);

    void add(Product product);

    void update(Product product);

    Product findById(Integer id);

    Product findEnd(Integer id);
}
