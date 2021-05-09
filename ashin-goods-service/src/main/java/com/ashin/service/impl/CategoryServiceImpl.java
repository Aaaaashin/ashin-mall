package com.ashin.service.impl;

import com.ashin.mapper.CategoryMapper;
import com.ashin.pojo.Category;
import com.ashin.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> findByParentId(Integer parentId) {
        Category category = new Category();
        category.setParentId(parentId);

        List<Category> list = categoryMapper.select(category);
        return list;
    }

    @Override
    public List<Category> findAll() {
        return categoryMapper.selectAll();
    }

    @Override
    public List<Category> findCategoryWithLevel() {
        return categoryMapper.findCategoryWithLevel();
    }

    @Override
    public Category findById(Integer id) {
        return categoryMapper.selectByPrimaryKey(id);
    }
}