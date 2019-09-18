package com.neuedu.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.OrderItem;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IOrderItemService;
import com.neuedu.service.IOrderservice;

import com.neuedu.utils.BigDecimalUtils;
import com.neuedu.viewObject.CartOrderItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/portal/order/")
public class S_OrderController {
    @Autowired
    IOrderservice iOrderservice;
    @Autowired
    IOrderItemService iOrderItemservice;
    //创建订单
    @RequestMapping("createOrder")
    public ServerResponse createOrder (HttpSession session,Integer shippingId){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail("请您先进行登录");
        }
        return iOrderservice.createOrder(userInfo.getId(),shippingId);
    }

    //取消订单
    @RequestMapping("cancelOrder")
    public ServerResponse cancelOrder (HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail("请您先进行登录");
        }
        return iOrderservice.cancelOrder(userInfo.getId(),orderNo);
    }

    //查询订单中的商品信息
    @RequestMapping("searchProductFromOrder")
    public ServerResponse searchProductFromOrder (HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail("请您先进行登录");
        }
        return iOrderservice.searchProductFromOrder(userInfo.getId(),orderNo);
    }

    //查询订单列表
    @RequestMapping("searchOrderList")
    public ServerResponse searchOrderList (HttpSession session,
                                           @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                           @RequestParam(value = "size",required = false,defaultValue = "10") Integer size){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail("请您先进行登录");
        }
        return iOrderservice.searchOrderList(userInfo.getId(),pageNum,size);
    }

    //查询某一订单详情
    @RequestMapping("searchOrderMessage")
    public ServerResponse searchOrderMessage (HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail("请您先进行登录");
        }
        return iOrderservice.searchOrderMessage(userInfo.getId(),orderNo);
    }


    //支付接扣
    @RequestMapping("pay")
    public ServerResponse pay(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail("请您先进行登录");
        }
        return iOrderservice.pay(userInfo.getId(),orderNo);
    }


    //支付宝服务器回调商家服务器的接口
    @RequestMapping("alipayCallBack")
    public ServerResponse callback(HttpServletRequest request,HttpSession session) {
       /* System.out.println("==============callback");
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail("请您先进行登录");
        }*/
        //封装支付宝的参数到map中，
        Map<String,String[]> params=request.getParameterMap();
        Map<String ,String> requestparams= Maps.newHashMap();
        Iterator<String> iterator= params.keySet().iterator();
        while(iterator.hasNext()){
            String key=iterator.next();
            String[] strings=params.get(key);
            String value="";
            for(int i=0;i<strings.length;i++){
                value=(i==strings.length-1)?value+strings[i]:value+strings[i]+",";
            }
            requestparams.put(key,value);
        }

        //支付宝的验签
        try {
            requestparams.remove("sign_type");
            boolean result=AlipaySignature.rsaCheckV2(requestparams, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if(!result){
                return ServerResponse.createServerResponseByFail("非法请求，验证不通过");
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        //处理业务逻辑，交给service
        return iOrderservice.callBack(requestparams);
        //return null;
    }


    //查看支付状态
    @RequestMapping("searPayStatus")
    public ServerResponse searPayStatus(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail("请您先进行登录");
        }
        return iOrderservice.searPayStatus(orderNo);
    }

    @RequestMapping("alterAddress")
    public ServerResponse alterAddress(HttpSession session,Integer shippingId,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail("请您先进行登录");
        }
        return iOrderservice.alterAddress(userInfo.getId(),orderNo,shippingId);
    }
    @RequestMapping("findMainOrder")
    public ServerResponse findMainOrder(Long orderNo){

        List<OrderItem> list =iOrderItemservice.findOrderItemsByOrderNo(orderNo);
        BigDecimal total = getOrderPrice(list);
        CartOrderItemVO cartOrderItemVO=new CartOrderItemVO();
        cartOrderItemVO.setTotalPrice(total);
        cartOrderItemVO.setOderItem(list);
        return ServerResponse.createServerResponseBySucess("成功",cartOrderItemVO);
    }

    private BigDecimal getOrderPrice(List<OrderItem> orderItemList){
        BigDecimal bigDecimal=new BigDecimal("0");
        for(OrderItem orderItem:orderItemList){
            bigDecimal= BigDecimalUtils.add(bigDecimal.doubleValue(),orderItem.getTotalPrice().doubleValue());
        }
        return bigDecimal;
    }
}
