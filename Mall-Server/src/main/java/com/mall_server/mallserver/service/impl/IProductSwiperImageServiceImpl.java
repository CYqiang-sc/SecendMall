package com.mall_server.mallserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall_server.mallserver.entity.ProductSwiperImage;
import com.mall_server.mallserver.mapper.ProductSwiperImageMapper;
import com.mall_server.mallserver.service.IProductSwiperImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 产品轮播图片Service实现类
 */
@Service("productSwiperImageService")
public class IProductSwiperImageServiceImpl extends ServiceImpl<ProductSwiperImageMapper,ProductSwiperImage> implements IProductSwiperImageService {

    @Autowired
    private ProductSwiperImageMapper productSwiperImageMapper;
}
