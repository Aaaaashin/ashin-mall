package com.ashin.service;

import com.ashin.entity.PageResult;
import com.ashin.entity.QueryPageBean;
import com.ashin.entity.Result;
import com.ashin.pojo.Goods;

import java.util.List;

public interface GoodsService {

    PageResult findPage(QueryPageBean queryPageBean);

    Goods findById(Integer id);

    void add(Goods goods);

    void upGoods(Integer id) throws Exception;

    void downGoods(Integer id) throws Exception;

    void delete(Integer id);

    void updateStatus(Integer id, String status);

    List<Goods> findAll();

    Result buy(Integer goodId, Integer buyNum);
}