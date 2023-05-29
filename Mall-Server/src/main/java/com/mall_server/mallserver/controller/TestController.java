package com.mall_server.mallserver.controller;

import com.mall_server.mallserver.entity.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 */
@RestController
@RequestMapping("/mall_server")
public class TestController {

    /**
     * 测试
     * @return
     */
    @GetMapping("/test")
    public R test(){
        return R.ok("成功secondmall");
    }
}
