package com.neuedu.service.impl;

import com.neuedu.dao.UserInfoMapper;
import com.neuedu.exception.MyException;
import com.neuedu.pojo.UserInfo;
import com.neuedu.pojo.UserPage;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public UserInfo login(UserInfo userInfo) throws MyException {

        //step1:参数的非空校验
        UserInfo userInfo1=new UserInfo();

        if(userInfo.getUsername()==null||userInfo.getUsername().equals("")){
            userInfo1.setWrongMsg("用户名不能为空");
            return userInfo1;
        }
        if(userInfo.getPassword()==null||userInfo.getPassword().equals("")){
            userInfo1.setWrongMsg("密码不能为空");
            return userInfo1;
        }
//        if(userInfo==null){
//            throw  new MyException("参数不能为空");
//        }
//        if(userInfo.getUsername()==null||userInfo.getUsername().equals("")){
//            throw  new MyException("用户名不能为空");
//        }
//        if(userInfo.getPassword()==null||userInfo.getPassword().equals("")){
//            throw  new MyException("密码不能为空");
//        }
        //step2:判断用户名是否存在

       int username_result= userInfoMapper.exsitsUsername(userInfo.getUsername());

//        if(username_result==0){//用户名不存在
//
//            throw  new MyException("用户名不存在");
//        }


        //step3: 根据用户名和密码登录
        UserInfo userinfo_result= userInfoMapper.findByUsernameAndPassword(userInfo);

        if(username_result==0){//用户名不存在
            System.out.println("用户名不存在");

            userInfo1.setWrongMsg("用户名不存在");
            return userInfo1;
        }

//        if(userinfo_result==null){
//            throw  new MyException("密码错误");
//        }
        if(userinfo_result==null){
            System.out.println("密码错误");
            userInfo1.setWrongMsg("密码错误");
            return userInfo1;
        }

        if(userinfo_result.getRole()!=0){//不是管理员
            System.out.println("没有权限");
            userInfo1.setWrongMsg("没有权限访问");
            return userInfo1;
        }
        //step4: 判断权限

//        if(userinfo_result.getRole()!=0){//不是管理员
//            throw  new MyException("没有权限访问");
//        }


        return userinfo_result;
    }

    @Override
    public UserPage findByFenYe(int pageNum, int pageSize) {
        List<UserInfo> userInfo=userInfoMapper.findByFenYe((pageNum-1)*pageSize,pageSize);
        UserPage userPage=new UserPage();
        userPage.setList(userInfo);
        return userPage;



    }
}
