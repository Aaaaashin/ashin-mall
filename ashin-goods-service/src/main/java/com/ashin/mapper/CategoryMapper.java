package com.ashin.mapper;

import com.ashin.pojo.Category;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<Category> {

    public List<Category> findCategoryWithLevel();

}