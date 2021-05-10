package com.ashin.order.service;

import com.ashin.entity.Result;
import com.ashin.pojo.Order;

public interface OrderService {

    Result getSubmitInfo(Integer goodId);

    Result submitOrder(Order order);

    Result findById(Long orderId);

    Result updateStatus(Long orderId, String s);

}
