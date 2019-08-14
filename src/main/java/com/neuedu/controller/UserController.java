package com.neuedu.controller;


import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.pojo.UserPage;
import com.neuedu.service.IUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController   //让返回值为字符串
@RequestMapping("/manage/user/")
public class UserController {

    @Autowired
    IUserService userService;
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    public ServerResponse login(UserInfo userInfo){

        UserInfo loginUserInfo= userService.login(userInfo);
        System.out.println(loginUserInfo.getWrongMsg()+"=========");
        if (loginUserInfo.getWrongMsg()==null){
            return ServerResponse.createServerResponseBySucess(loginUserInfo);
        }else{
            return ServerResponse.createServerResponseByFail(1,loginUserInfo.getWrongMsg());
        }
        //return ServerResponse.createServerResponseByFail(1,loginUserInfo.getWrongMsg());
    }
    @RequestMapping(value = "list.do/{pageNum}/{pageSize}",method = RequestMethod.POST)
    public ServerResponse list(@PathVariable("pageNum") int pageNum,
                               @PathVariable("pageSize") int pageSize, HttpSession session){

        //补充失败的情况
        UserPage userPage=userService.findByFenYe(pageNum,pageSize);

        if (userPage!=null){
            return ServerResponse.createServerResponseBySucess(userPage);
        }else{
            return ServerResponse.createServerResponseByFail(1,"失败");
        }
        //return ServerResponse.createServerResponseByFail(1,loginUserInfo.getWrongMsg());
    }

}
