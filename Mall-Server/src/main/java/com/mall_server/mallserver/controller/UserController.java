package com.mall_server.mallserver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall_server.mallserver.constant.SystemConstant;
import com.mall_server.mallserver.entity.Audit;
import com.mall_server.mallserver.entity.R;
import com.mall_server.mallserver.entity.WxUserInfo;
import com.mall_server.mallserver.properties.WeixinProperties;
import com.mall_server.mallserver.service.IWxUserInfoService;
import com.mall_server.mallserver.util.HttpClientUtil;
import com.mall_server.mallserver.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信用户Controller
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private WeixinProperties weixinProperties;

    @Autowired
    private HttpClientUtil httpClientUtil;

    @Autowired
    private IWxUserInfoService wxUserInfoService;


    /**
     * 微信用户登录
     * @return
     */
    @RequestMapping("/wxlogin")
    public R wxLogin(@RequestBody WxUserInfo wxUserInfo){

        String jscode2sessionUrl=weixinProperties.getJscode2sessionUrl()+"?appid="+weixinProperties.getAppid()+"&secret="+weixinProperties.getSecret()+"&js_code="+wxUserInfo.getCode()+"&grant_type=authorization_code";

        String result = httpClientUtil.sendHttpGet(jscode2sessionUrl);
        JSONObject jsonObject= JSON.parseObject(result);
        String openid = jsonObject.get("openid").toString();

        WxUserInfo resultWxUserInfo = wxUserInfoService.getOne(new QueryWrapper<WxUserInfo>().eq("openid", openid));
        if(resultWxUserInfo==null){
            System.out.println("不存在 插入用户");
            wxUserInfo.setOpenid(openid);
            wxUserInfo.setRegisterDate(new Date());
            wxUserInfo.setLastLoginDate(new Date());
            wxUserInfoService.save(wxUserInfo);
        }else{
            System.out.println("存在 更新用户");
            resultWxUserInfo.setNickName(wxUserInfo.getNickName());
            resultWxUserInfo.setAvatarUrl(wxUserInfo.getAvatarUrl());
            resultWxUserInfo.setLastLoginDate(new Date());
            wxUserInfoService.updateById(resultWxUserInfo);
        }

        String token = JwtUtils.createJWT(openid, wxUserInfo.getNickName(), SystemConstant.JWT_TTL);
        Integer userid =resultWxUserInfo.getId();
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("token",token);
        resultMap.put("userId",userid);
        return R.ok(resultMap);
    }

    /**
     * 用户商品审核
     * @return
     */
    @RequestMapping("/userProduct")
    public R wxLogin(@RequestBody Audit audit){
        System.out.println(new QueryWrapper<Audit>().eq("userId", 1));

        // 利用jwt生成token返回到前端

        Map<String,Object> resultMap=new HashMap<>();

        return R.ok(resultMap);
    }
}



