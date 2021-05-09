package com.ashin.service;

import com.ashin.entity.PageResult;
import com.ashin.entity.QueryPageBean;
import com.ashin.pojo.Brand;

import java.util.List;

public interface BrandService {

    PageResult findPage(QueryPageBean queryPageBean);

    List<Brand> findAll();

    Brand findById(Integer id);
}