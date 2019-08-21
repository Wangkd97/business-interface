package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface IOrderservice {
    //springboot创建订单
    public ServerResponse createOrder(Integer userId, Integer shippingId);

    // springboot 取消订单
    public ServerResponse cancelOrder(Integer userId, Long orderNo);

    //Springboot 查看订单中的商品信息
    public ServerResponse searchProductFromOrder(Integer userId, Long orderNo);

    //springboot 查看订单列表
    public ServerResponse searchOrderList(Integer userId, Integer pageNum, Integer size);

    //soringboot 查询某一订单详情
    public ServerResponse searchOrderMessage(Integer userId, Long orderNo);


    ////////////////////////////支付模块///////////////////////////

    //springboot 支付、
    public ServerResponse pay(Integer userId, Long orderNo);

    //springboot 支付宝回调接口
    public ServerResponse callBack(Map<String, String> map);

    //springboot 查看支付状态
    public ServerResponse searPayStatus(Long orderNo);
}
