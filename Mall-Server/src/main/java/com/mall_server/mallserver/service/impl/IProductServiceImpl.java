package com.mall_server.mallserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall_server.mallserver.entity.Product;
import com.mall_server.mallserver.mapper.ProductMapper;
import com.mall_server.mallserver.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 商品Service实现类
 */
@Service("productService")
public class IProductServiceImpl extends ServiceImpl<ProductMapper,Product> implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> list(Map<String, Object> map) {
        List<Product> list = productMapper.list(map);
        return list;
    }

    @Override
    public Long getTotal(Map<String, Object> map) {
        return productMapper.getTotal(map);
    }

    @Override
    public void add(Product product) {
         productMapper.add(product);
    }

    @Override
    public void update(Product product) {
        productMapper.update(product);
    }

    @Override
    public Product findById(Integer id) {
        return productMapper.findById(id);
    }
    @Override
    public Product findEnd(Integer id) {
        return productMapper.findEnd(id);
    }
}
