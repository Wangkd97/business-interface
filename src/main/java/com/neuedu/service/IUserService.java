package com.neuedu.service;

import com.neuedu.exception.MyException;
import com.neuedu.pojo.UserInfo;
import com.neuedu.pojo.UserPage;

import java.util.List;

public interface IUserService {

    public UserInfo login(UserInfo userInfo) throws MyException;
    UserPage findByFenYe(int pageNum, int pageSize);

}
