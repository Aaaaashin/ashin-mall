package com.ashin.order.service.impl;

import com.ashin.entity.Result;
import com.ashin.order.feign.GoodsFeign;
import com.ashin.order.feign.UserFeign;
import com.ashin.order.mapper.OrderMapper;
import com.ashin.order.service.OrderService;
import com.ashin.pojo.Address;
import com.ashin.pojo.Goods;
import com.ashin.pojo.Order;
import com.ashin.util.ShopConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserFeign userFeign;

    @Autowired
    private GoodsFeign goodsFeign;

    @Autowired
    private OrderMapper orderMapper;


    @Override
    public Result getSubmitInfo(Integer goodId) {
        Map map = new HashMap();

        //获取当前登陆用户，未登陆提示登陆
        //根据登陆用户，查询用户微服务中的地址列表信息
        List<Address> addressList = userFeign.findByUserId(ShopConstant.LOGIN_USER_NAME);
        //根据商品Id 查询商品信息（价格 图片 标题）
        Result<Goods> goodsResult = goodsFeign.findByGoodId(goodId);
        //获得商品数据
        Goods goods = goodsResult.getData();

        //封装 返回
        map.put("addressList", addressList);
        map.put("goods", goods);
        return new Result(true, "查询成功", map);
    }

    @Override
    public Result submitOrder(Order order) {
        //创建订单
        if (order == null || order.getOrderId() == null) {
            return new Result(false, "订单信息错误");
        }

        Result buyResult = goodsFeign.buy(order.getGoodId(),1);
        if (!buyResult.isFlag()) {
            return buyResult;
        }

        order.setStatus("1");
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
//        order.setPaymentTime(new Date());
        order.setUserId(ShopConstant.LOGIN_USER_NAME);
        //远程调用 商品扣减库存方法（判断数量是否大于购买数量， 扣减库存）
        orderMapper.insert(order);
        return new Result(true, "创建订单成功", String.valueOf(order.getOrderId()));
    }

    @Override
    public Result findById(Long orderId) {
        //查询订单
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            return new Result(false, "未查询到订单信息");
        }
        return new Result(true, "查询成功", order);
    }

    @Override
    public Result updateStatus(Long orderId, String status) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            return new Result(false, "未查询到订单信息");
        }
        if (!"1".equals(order.getStatus())) {
            return new Result(false, "订单状态已变更");
        }
        order.setStatus("2");
        order.setPaymentTime(new Date());
        orderMapper.updateById(order);
        return new Result(true, "订单状态修改为已支付");
    }
}
