package com.neuedu.service.impl;

import com.neuedu.common.ServerResponse;
import com.neuedu.consts.Const;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.exception.MyException;
import com.neuedu.pojo.UserInfo;
import com.neuedu.pojo.UserPage;
import com.neuedu.service.IUserService;
import com.neuedu.utils.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

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


    /**
     * copy
     */

//    @Override
//    public int findByUserName(String username) {
//        int rows=userInfoMapper.findByUserName(username);
//        return rows;
//    }

//    @Override
//    public UserInfo findByUserNameAndPassword(String username, String password) {
//        //System.out.println("========serviceImpl======");
//        UserInfo userInfo=userInfoMapper.findByUserNameAndPassword(username,password);
//        //System.out.println("========serviceImpl======"+userInfo);
//        return userInfo;
//    }
//    @Override
//    public List<UserInfo> findByRole(int role){
//        List<UserInfo> list =userInfoMapper.findByRole(role);
//        return list;
//    }
    //注册接口
    @Override
    public ServerResponse register(UserInfo userInfo) {

        //参数非空校验
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(100,"注册信息不能为空");
        }
        //校验用户名
        int rows = userInfoMapper.findByUserName(userInfo.getUsername());
        if(rows>0){
            return ServerResponse.createServerResponseByFail(1,"用户名已存在");
        }
        //校验邮箱
        int emailRows=userInfoMapper.findByEmail(userInfo.getEmail());
        if(emailRows>0){
            return ServerResponse.createServerResponseByFail(2,"邮箱已注册");
        }
        //注册
        userInfo.setRole(0);
        //userInfo.setPassword(MD5Utils.getMD5Code(userInfo.getPassword()));
        int insertNum =userInfoMapper.insert(userInfo);
        if(insertNum>0){
            return ServerResponse.createServerResponseBySucess(0,"注册成功");
        }else{
            return  ServerResponse.createServerResponseByFail(400,"注册失败");
        }
    }

    //根据用户名找回密保问题接口
    @Override
    public ServerResponse forgetPasswordGetQuestion(String username) {
        //参数校验
        if(username==null||username.equals("")){
            return  ServerResponse.createServerResponseByFail(100,"用户名为空");
        }
        //校验用户名
        int row=userInfoMapper.findByUserName(username);
        if(row==0){
            return ServerResponse.createServerResponseByFail(100,"用户不存在");
        }
        //查找密保问题
        String question=userInfoMapper.findQuestionByUaername(username);
        return ServerResponse.createServerResponseBySucess(question);
    }

    //校验用户提交答案接口
    @Override
    public ServerResponse forgetPasswordCheckAnswer(String username, String question, String answer) {
        //参数校验
        if(username==null||username.equals("")){
            return  ServerResponse.createServerResponseByFail(100,"用户名为空");
        }
        if(question==null||question.equals("")){
            return  ServerResponse.createServerResponseByFail(100,"问题名为空");
        }
        if(answer==null||answer.equals("")){
            return  ServerResponse.createServerResponseByFail(100,"答案为空");
        }
        //根据username question answer查询
        int rows=userInfoMapper.forgetPasswordCheckAnswer(username,question,answer);
        if(rows==0){
            return ServerResponse.createServerResponseByFail(0,"答案错误");
        }
        //服务端生成一个token，并且返回给客户端
        String forgetToken= UUID.randomUUID().toString();
        //guavaCache
        TokenCache.set(username,forgetToken);
        return ServerResponse.createServerResponseBySucess(forgetToken);
    }

    //springboot 忘记密码的重置密码
    @Override
    public ServerResponse forgetPasswordSetPassword(String username, String passwordNew, String forgetToken) {
        //参数校验
        if(username==null||username.equals("")){
            return  ServerResponse.createServerResponseByFail(100,"用户名不能为空");
        }
        if(passwordNew==null||passwordNew.equals("")){
            return  ServerResponse.createServerResponseByFail(100,"新密码不能为空");
        }
        if(forgetToken==null||forgetToken.equals("")){
            return  ServerResponse.createServerResponseByFail(100,"forgetToken为空");
        }
        //token校验
        String token = TokenCache.get(username);
        if(token==null){
            return ServerResponse.createServerResponseByFail(0,"token过期");
        }
        if(!token.equals(forgetToken)){
            return ServerResponse.createServerResponseByFail(0,"无效的token");
        }
        //修改密码
        int rows=userInfoMapper.updatePassword(username,passwordNew);
        if(rows>0){
            return ServerResponse.createServerResponseBySucess("修改成功",username);
        }
        return ServerResponse.createServerResponseByFail(0,"密码修改失败");
    }

    @Override
    public ServerResponse getLoginUser(HttpSession session) {
        session.getAttribute(Const.CURRENT_USER);
        System.out.println("=====================session"+session.getAttribute(Const.CURRENT_USER));
        UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENT_USER);
        if(userInfo==null||userInfo.equals(null)){
            return ServerResponse.createServerResponseByFail(0,"用户尚未登录");
        }
        return ServerResponse.createServerResponseBySucess(userInfo);
    }

    @Override
    public ServerResponse loginUpdatePassword(String passwordNew,HttpSession session) {
        session.getAttribute(Const.CURRENT_USER);
        //System.out.println("=====================session"+session.getAttribute(Const.CURRENTUSER));
        UserInfo userInfo= (UserInfo) session.getAttribute(Const.CURRENT_USER);
        if(userInfo==null||userInfo.equals(null)){
            return ServerResponse.createServerResponseByFail(0,"用户尚未登录");
        }
        String username=userInfo.getUsername();
        int rows=userInfoMapper.updatePassword(username,passwordNew);
        if(rows>0){
            //删除session
            userInfo.setPassword(passwordNew);
            session.setAttribute(Const.CURRENT_USER,userInfo);
            return ServerResponse.createServerResponseBySucess(1,"修改成功");
        }
        return ServerResponse.createServerResponseByFail(0,"修改失败");
    }

    //springboot 登陆中修改个人信息
    @Override
    public ServerResponse loginUpdateUserInfo(UserInfo userInfo, HttpSession session) {
        session.getAttribute(Const.CURRENT_USER);
        UserInfo userInfoOld= (UserInfo) session.getAttribute(Const.CURRENT_USER);
        if(userInfo==null||userInfo.equals(null)){
            return ServerResponse.createServerResponseByFail(0,"用户尚未登录");
        }
        int newId=userInfoOld.getId();
        userInfo.setId(newId);
        int rows=userInfoMapper.updateByPrimaryKey(userInfo);
        if(rows>0){
            session.setAttribute(Const.CURRENT_USER,userInfo);
            return ServerResponse.createServerResponseBySucess(userInfo);
        }
        return ServerResponse.createServerResponseByFail(0,"修改失败");
    }


}
