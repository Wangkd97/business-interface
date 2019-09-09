package com.neuedu.controller;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.config.AppConfig;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IProductService;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class HelloController
{

    @Autowired
    IProductService iProductService;
    IUserService userService;
    @RequestMapping("/login")
    public UserInfo hello(UserInfo userInfo){


        userInfo.setUsername("123");
        userInfo.setPassword("123");
        UserInfo userInfo1=userService.login(userInfo);
        return userInfo1;
    }


//    @Value("${jdbc.driver}")
//    private  String driver;
//    @Value("${jdbc.username}")
//    private String username;
//    @Value("${jdbc.password}")
//    private String password;


    @Autowired
    AppConfig appConfig;


    @RequestMapping("/test")
    public  String  getDriver(){

        return appConfig.getDriver()+" "+appConfig.getUsername()+" "+appConfig.getPassword();
    }
    @RequestMapping("/res")
    public ServerResponse searchProductList(HttpSession session,
                                            @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize
    ){

        return iProductService.searchProductList(pageNum,pageSize);
    }



}
