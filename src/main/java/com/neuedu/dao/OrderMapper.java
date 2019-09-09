package com.neuedu.dao;

import com.neuedu.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order
     *
     * @mbggenerated
     */
    int insert(Order record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order
     *
     * @mbggenerated
     */
    Order selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order
     *
     * @mbggenerated
     */
    List<Order> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Order record);

    //根据orderNohe1userId查询订单
    Order findByUserIdAndOrderNo(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);

    //根据userId分页查询order
    List<Order> findOrderBuyUserId(@Param("userId") Integer userId,
                                   @Param("start") Integer start,
                                   @Param("size") Integer size);

    ////根据userId查询order的数量
   int findOrderCountBuyUserId(@Param("userId") Integer userId);

   //根据订单号查询订单
    Order findBuOrderNo(Long orderNo);


}