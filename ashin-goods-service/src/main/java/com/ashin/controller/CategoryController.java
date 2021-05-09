package com.ashin.controller;

import com.ashin.entity.Result;
import com.ashin.pojo.Category;
import com.ashin.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/findByParentId")
    public Result findByParentId(@RequestParam(required = true,defaultValue = "0") Integer parentId){
        List<Category> list = categoryService.findByParentId(parentId);
        return new Result(true,"查询成功", list);
    }

    @GetMapping("/findAll")
    public Result findAll(){
        List<Category> list = categoryService.findAll();
        return new Result(true,"查询成功", list);
    }

    @GetMapping("/findCategoryWithLevel")
    public Result findCategoryWithLevel(){
        List<Category> list = categoryService.findCategoryWithLevel();
        return new Result(true,"查询成功", list);
    }
    @GetMapping("/find/{id}")
    public Result<Category> findById(@PathVariable Integer id){
        Category category = categoryService.findById(id);
        return new Result(true,"查询成功", category);
    }

}