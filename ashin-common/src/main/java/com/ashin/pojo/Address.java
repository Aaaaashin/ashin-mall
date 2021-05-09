package com.ashin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * (TbAddress)表实体类
 * @author ashin
 */
@SuppressWarnings("serial")
@Data
@TableName("tb_address")
public class Address {
    @TableId(type = IdType.AUTO)
    private Long id;
    //用户ID
    private String userId;
    //省
    private String provinceId;
    //市
    private String cityId;
    //县/区
    private String townId;
    //手机
    private String mobile;
    //详细地址
    private String address;
    //联系人
    private String contact;
    //是否是默认 1默认 0否
    private String isDefault;
    //备注
    private String notes;
    //创建日期
    private Date createDate;
    //别名
    private String alias;
}