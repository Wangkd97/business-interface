package com.neuedu.controller;


import com.neuedu.common.ServerResponse;
import com.neuedu.consts.Const;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController   //让返回值为字符串
@RequestMapping("/user/")
@CrossOrigin  //跨域注解
public class CustomerController {

    @Autowired
    IUserService iUserInfoService;

    @RequestMapping("logout.do")
    public ServerResponse exit(HttpSession session){

        session.removeAttribute("user");
        if (session.getAttribute("user")==null){
            return ServerResponse.createServerResponseBySucess("退出成功");
        }else{
            return ServerResponse.createServerResponseByFail(1,"服务器异常");
        }

    }

    /**
     * copy
     */

    //springboot登录接口
    @RequestMapping("/login.do")
    public ServerResponse login(String username,String password,HttpSession session){
        UserInfo userInfo=new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(password);

        UserInfo userInfo1=iUserInfoService.login(userInfo);
        if(userInfo1!=null){
            //登陆成功之后,放入session
            session.setAttribute(Const.CURRENT_USER,userInfo1);
            return ServerResponse.createServerResponseBySucess("登录成功",userInfo1);
        }
        return ServerResponse.createServerResponseByFail(0,"登录失败");
    }


    //springboot注册接口
    @RequestMapping("/register.do")
    public ServerResponse register(UserInfo userInfo,HttpSession session){
        ServerResponse serverResponse=iUserInfoService.register(userInfo);
        return serverResponse;
    }

    //springboot忘记密码查密保问题
    @RequestMapping("/forget_get_question.do")
    public ServerResponse forgetPasswordGetQuestion(String username){
        System.out.println("==================================="+username);
        ServerResponse serverResponse=iUserInfoService.forgetPasswordGetQuestion(username);
        return serverResponse;
    }

    //springboot提交问题答案
    @RequestMapping("/forget_check_answer.do")
    public  ServerResponse  forgetPasswordCheckAnswer(String username, String question,String answer){
        ServerResponse serverResponse=iUserInfoService.forgetPasswordCheckAnswer(username,question,answer);
        return serverResponse;
    }

    //springboot 验证密保问题之后重置密码
    @RequestMapping("/forget_reset_password.do")
    public  ServerResponse  forgetPasswordSetPassword(String username, String passwordNew,String forgetToken){
        ServerResponse serverResponse=iUserInfoService.forgetPasswordSetPassword(username,passwordNew,forgetToken);
        return serverResponse;
    }

    //获取登录用户信息
    @RequestMapping("/get_user_info.do")
    public  ServerResponse getLoginUser(HttpSession session){
        ServerResponse serverResponse=iUserInfoService.getLoginUser(session);
        return serverResponse;
    }

    //登录中重置密码
    @RequestMapping("reset_password.do")
    public ServerResponse loginUpdatePassword(String passwordNew,HttpSession session){
        ServerResponse serverResponse= iUserInfoService.loginUpdatePassword(passwordNew,session);
        return serverResponse;
    }

    //登陆中修改个人信息
    @RequestMapping("update_information.do")
    public ServerResponse loginUpdateUserInfo(UserInfo userInfo,HttpSession session){
        ServerResponse serverResponse= iUserInfoService.loginUpdateUserInfo(userInfo,session);
        return serverResponse;
    }

}
