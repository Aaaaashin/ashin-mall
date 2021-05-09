package com.ashin.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
public class GoodsInfo {
    private Integer id;//商品id
    private String name;//商品名称
    private String image;//商品图片
    private Integer brandId;//品牌名称
    private Integer categoryId;//分类名称
    private String brandName;//品牌名称
    private String categoryName;//分类名称
    private Integer num;//库存数量
    private String spec;//规格
    private Map specMap;
    private Date createTime;//创建时间
    private Date updateTime;//更新时间
    private Integer price;//价格
    private String status;//商品状态 1-正常，2-下架
}