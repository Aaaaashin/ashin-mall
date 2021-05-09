package com.ashin.service;

import com.ashin.entity.PageResult;
import com.ashin.entity.QueryPageBean;

import java.util.List;
import java.util.Map;

public interface SpecService {

    PageResult findPage(QueryPageBean queryPageBean);

    List<Map> findAllSpecWithOptions(Integer category3id);
}