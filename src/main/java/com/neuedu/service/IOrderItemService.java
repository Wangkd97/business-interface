package com.neuedu.service;

import com.neuedu.pojo.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOrderItemService {
    public List<OrderItem> findOrderItemsByOrderNo(Long orderNo);
}
