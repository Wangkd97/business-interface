package com.neuedu.service.impl;

import com.neuedu.dao.OrderItemMapper;
import com.neuedu.pojo.OrderItem;
import com.neuedu.service.IOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OderItemImpl implements IOrderItemService {

    @Autowired
    OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItem> findOrderItemsByOrderNo(Long orderNo) {
        List<OrderItem> list = orderItemMapper.findOrderItemsByOrderNo(orderNo);
        return list;
    }
}
