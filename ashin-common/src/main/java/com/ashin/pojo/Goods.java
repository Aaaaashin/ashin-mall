package com.ashin.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@NoArgsConstructor
@Table(name = "tb_goods")
@TableName("tb_goods")
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//商品id
    private String sn;//商品条码
    private String name;//商品名称
    private String caption;//描述
    private Integer price;//价格（分）
    private Integer num;//库存数量
    private String image;//商品图片
    private String images;//商品图片列表
    private String category;//分类名称
    private String brand;//品牌名称
    private String spec;//规格
    private Integer saleNum;//销量
    private Integer commentNum;//评论数
    private String isMarketable;//是否上架
    private String status;//商品状态 1-正常，2-下架
    private Date createtime;//创建时间
    private Date updatetime;//更新时间
}