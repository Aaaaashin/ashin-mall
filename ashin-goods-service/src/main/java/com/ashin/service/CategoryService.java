package com.ashin.service;

import com.ashin.pojo.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findByParentId(Integer parentId);

    List<Category> findAll();

    List<Category> findCategoryWithLevel();

    Category findById(Integer id);
}