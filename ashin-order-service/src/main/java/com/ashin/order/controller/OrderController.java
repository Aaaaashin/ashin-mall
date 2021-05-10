package com.ashin.order.controller;

import com.alibaba.fastjson.JSON;
import com.ashin.entity.Result;
import com.ashin.order.service.OrderService;
import com.ashin.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("submitInfo/{goodId}")
    public Result getSubmitInfo(@PathVariable Integer goodId) {
        return orderService.getSubmitInfo(goodId);
    }

    @PostMapping("submitOrder")
    public Result submitOrder(@RequestBody Order order) {
        System.out.println(JSON.toJSONString(order));
        return orderService.submitOrder(order);
    }

    @GetMapping("findById/{orderId}")
    public Result findById(@PathVariable Long orderId) {
        return orderService.findById(orderId);
    }

    @PutMapping("paySuccess/{orderId}")
    public Result paySuccess(@PathVariable Long orderId) {
        //修改订单状态为 已支付
        return orderService.updateStatus(orderId, "2");
    }
}
