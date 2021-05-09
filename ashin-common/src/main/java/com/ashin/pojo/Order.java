package com.ashin.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * (TbOrder)表实体类
 * @author ashin
 */
@SuppressWarnings("serial")
@Data
@TableName("tb_order")
public class Order{
    //订单id
    @TableId(value = "order_id",type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;
    //实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分
    private Double payment;
    //支付类型，1、在线支付，2、货到付款
    private String paymentType;
    //状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
    private String status;
    //订单创建时间
    private Date createTime;
    //订单更新时间
    private Date updateTime;
    //付款时间
    private Date paymentTime;
    //用户id
    private String userId;
    //收货人地区名称(省，市，县)街道
    private String receiverAreaName;
    //收货人手机
    private String receiverMobile;
    //收货人
    private String receiver;
    //订单商品ID
    private Integer goodId;
    //图片路径
    private String picPath;
}