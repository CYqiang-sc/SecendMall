package com.mall_server.mallserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall_server.mallserver.constant.SystemConstant;
import com.mall_server.mallserver.entity.Audit;
import com.mall_server.mallserver.entity.Order;
import com.mall_server.mallserver.entity.OrderDetail;
import com.mall_server.mallserver.entity.R;
import com.mall_server.mallserver.properties.WeixinpayProperties;
import com.mall_server.mallserver.service.IAuditService;
import com.mall_server.mallserver.service.IOrderDetailService;
import com.mall_server.mallserver.service.IOrderService;
import com.mall_server.mallserver.service.IWxUserInfoService;
import com.mall_server.mallserver.service.impl.IAuditServiceImpl;
import com.mall_server.mallserver.util.*;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import io.jsonwebtoken.Claims;
import javafx.scene.input.TouchEvent;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.util.*;

/**
 * 订单Controller控制器
 */
@RestController
@RequestMapping("/my/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderDetailService orderDetailService;

    @Autowired
    private IAuditService aduitService;

    /**
     * 创建订单，返回订单号
     * @param token
     * @return
     */
    @RequestMapping("/create")
    @Transactional
    public R create(@RequestBody Order order, @RequestHeader(value = "token")String token){
        // 通过token获取openid
//        System.out.println("token="+token);
//        System.out.println("order="+order);
        // 添加订单到数据库
        Claims claims = JwtUtils.validateJWT(token).getClaims();
        if(claims!=null){
//            System.out.println("openid="+claims.getId());
            order.setUserId(claims.getId());
        }
        order.setOrderNo("JAVA"+DateUtil.getCurrentDateStr());
        order.setCreateDate(new Date());

        OrderDetail[] goods = order.getGoods();
        orderService.save(order);
        // 添加订单详情到数据库
        for(int i=0;i<goods.length;i++){
            OrderDetail orderDetail=goods[i];
            orderDetail.setMId(order.getId());
            orderDetailService.save(orderDetail);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("orderNo",order.getOrderNo());
        return R.ok(resultMap);
    }
    /**
     * 调用统一下单，预支付
     * @param orderNo
     * @return
     * @throws Exception
     */
    @RequestMapping("/preparePay")
    public R preparePay(@RequestBody String orderNo)throws Exception{
//        System.out.println("orderNo="+orderNo);
        Order order = orderService.getOne(new QueryWrapper<Order>().eq("orderNo", orderNo));
//        System.out.println("openid="+order.getUserId());
        Map<String,Object> payMap=new HashMap<>();
        payMap.put("appId",1);
        payMap.put("timeStamp",System.currentTimeMillis()/1000+"");
        payMap.put("nonceStr",1);
        payMap.put("package",1);
        payMap.put("signType","MD5");
        payMap.put("paySign",1);
        payMap.put("orderNo",orderNo);

        return R.ok(payMap);
    }
    /**
     * 调用统一下单，成功支付
     * @param orderNo
     * @return
     * @throws Exception
     */
    @RequestMapping("/successPay")
    public R successPay(@RequestBody String orderNo)throws Exception{
//        System.out.println("orderNo="+orderNo);
        Order order = orderService.getOne(new QueryWrapper<Order>().eq("orderNo", orderNo));
        order.setPayDate(new Date());
        order.setStatus(2); // 设置已经支付状态
        orderService.saveOrUpdate(order);
        return R.ok();
    }

    /**
     * 订单查询 type值 0 全部订单  1 待付款  2 待收货 3 退款/退货
     * @param type
     * @return
     */
    @RequestMapping("/list")
    public R list(Integer type,Integer page,Integer pageSize){
//        System.out.println("type="+type);

        List<Order> orderList=null;
        Map<String,Object> resultMap=new HashMap<>();

        Page<Order> pageOrder=new Page<>(page,pageSize);

        if(type==0){ // 全部订单查询
            // orderList=orderService.list(new QueryWrapper<Order>().orderByDesc("id"));
            Page<Order> orderResult = orderService.page(pageOrder, new QueryWrapper<Order>().orderByDesc("id"));
//            System.out.println("总记录数"+orderResult.getTotal());
//            System.out.println("总页数"+orderResult.getPages());
//            System.out.println("当前页数据"+orderResult.getRecords());
            orderList=orderResult.getRecords();
            resultMap.put("total",orderResult.getTotal());
            resultMap.put("totalPage",orderResult.getPages());

        }else{
            // orderList=orderService.list(new QueryWrapper<Order>().eq("status",type).orderByDesc("id"));
            Page<Order> orderResult = orderService.page(pageOrder, new QueryWrapper<Order>().eq("status",type).orderByDesc("id"));
//            System.out.println("总记录数"+orderResult.getTotal());
//            System.out.println("总页数"+orderResult.getPages());
//            System.out.println("当前页数据"+orderResult.getRecords());
            orderList=orderResult.getRecords();
            resultMap.put("total",orderResult.getTotal());
            resultMap.put("totalPage",orderResult.getPages());
        }
        QueryWrapper<Audit> warp =  new QueryWrapper<Audit>().eq("userId", 1).orderByDesc("id");
        List<Audit> one = aduitService.list(warp);
        System.out.println("www\n"+one);
        resultMap.put("orderList",orderList);
        return R.ok(resultMap);
    }

    /**
     * 用户查看商品审核
     * @param
     * @return
     */
    @RequestMapping("/auditList")
    public R auditlist(Integer userId){
        List<Audit> orderList=null;
        Map<String,Object> resultMap=new HashMap<>();

        Page<Audit> pageOrder=new Page<>(1,10);
        Page<Audit> orderResult = aduitService.page(pageOrder, new QueryWrapper<Audit>().eq("userId", userId).orderByDesc("id"));
        orderList=orderResult.getRecords();
        resultMap.put("orderList",orderList);
        return R.ok(resultMap);
    }

}
