package com.ashin.controller;

import com.ashin.entity.PageResult;
import com.ashin.entity.QueryPageBean;
import com.ashin.entity.Result;
import com.ashin.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/spec")
public class SpecController {

    @Autowired
    private SpecService specService;

    @GetMapping("/findPage")
    public PageResult findPage(QueryPageBean queryPageBean){
        PageResult pageResult = specService.findPage(queryPageBean);
        return pageResult;
    }

    @GetMapping("/findAllSpecWithOptions")
    public Result findAllSpecWithOptions(Integer category3id){
       List<Map> resultList = specService.findAllSpecWithOptions(category3id);
       return new Result(true,"查询成功",resultList);
    }
}