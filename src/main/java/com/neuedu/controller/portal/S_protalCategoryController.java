package com.neuedu.controller.portal;


import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Category;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/portal/category/")
@CrossOrigin
public class S_protalCategoryController {
    @Autowired
    ICategoryService iCategoryService;

    @RequestMapping("findFatherCategory.do")
    public ServerResponse findChildCategory(){
        List<Category> list = iCategoryService.findFatherCategory();
        return ServerResponse.createServerResponseBySucess(list);
    }




}
