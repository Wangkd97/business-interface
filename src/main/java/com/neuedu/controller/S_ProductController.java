package com.neuedu.controller;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manger/product/")
public class S_ProductController {
    @Autowired
    IProductService iProductService;

    //新增或更新商品
    @RequestMapping("addOrUpdateProduct")
    public ServerResponse addOrUpdateProduct(Product product, HttpSession session){

        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        System.out.println("==================="+userInfo);
        if(userInfo==null){
            System.out.println("================================null");
            return ServerResponse.createServerResponseByFail(Const.ResopnseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if(userInfo.getRole()!=Const.RoleEnum.ROLE_ADMINE.getCode()){
            return ServerResponse.createServerResponseByFail(Const.ResopnseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        System.out.println("=====================================14");
        return iProductService.addOrUpdate(product);
    }

    //产品的上下架
    @RequestMapping("setStatus")
    public ServerResponse setStatus(Integer productId, Integer status,HttpSession session){

        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);

        if(userInfo==null){

            return ServerResponse.createServerResponseByFail(Const.ResopnseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if(userInfo.getRole()!=Const.RoleEnum.ROLE_ADMINE.getCode()){
            return ServerResponse.createServerResponseByFail(Const.ResopnseCodeEnum.NO_PRIVILEGE.getDesc());
        }

        return iProductService.setStatus(productId,status);
    }

    //查询商品详细信息
    @RequestMapping("getProductMessage")
    public ServerResponse getProductMessage(Integer productId, Integer status,HttpSession session){

        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);

        if(userInfo==null){

            return ServerResponse.createServerResponseByFail(Const.ResopnseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if(userInfo.getRole()!=Const.RoleEnum.ROLE_ADMINE.getCode()){
            return ServerResponse.createServerResponseByFail(Const.ResopnseCodeEnum.NO_PRIVILEGE.getDesc());
        }

        return iProductService.getProductMessage(productId);
    }

    //分页查询商品列表
    @RequestMapping("searchProductList")
    public ServerResponse searchProductList(HttpSession session,
                                            @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize
                                            ){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResopnseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if(userInfo.getRole()!=Const.RoleEnum.ROLE_ADMINE.getCode()){
            return ServerResponse.createServerResponseByFail(Const.ResopnseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return iProductService.searchProductList(pageNum,pageSize);
    }

    //后台模糊查询商品列表
    @RequestMapping("searchProduct")
    public ServerResponse searchproduct(HttpSession session,
                                        @RequestParam(value = "productId",required = false) Integer productId,
                                        @RequestParam(value = "productName",required = false) String productName,
                                        @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                        @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize
    ){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResopnseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if(userInfo.getRole()!=Const.RoleEnum.ROLE_ADMINE.getCode()){
            return ServerResponse.createServerResponseByFail(Const.ResopnseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return iProductService.searchProduct(productId,productName,pageNum,pageSize);
    }
}
