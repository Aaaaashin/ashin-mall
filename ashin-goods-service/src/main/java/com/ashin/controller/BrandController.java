package com.ashin.controller;

import com.ashin.entity.PageResult;
import com.ashin.entity.QueryPageBean;
import com.ashin.entity.Result;
import com.ashin.pojo.Brand;
import com.ashin.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("/findPage")
    public PageResult findPage(QueryPageBean queryPageBean){
       PageResult pageResult = brandService.findPage(queryPageBean);
       return pageResult;
    }

    @GetMapping("/findAll")
    public Result findAll(){
        List<Brand> brandList = brandService.findAll();
        return  new Result(true,"查询成功", brandList);
    }


    @GetMapping("/find/{id}")
    public Result<Brand> findById(@PathVariable Integer id){
        Brand brand = brandService.findById(id);
        return  new Result(true,"查询成功", brand);
    }

}