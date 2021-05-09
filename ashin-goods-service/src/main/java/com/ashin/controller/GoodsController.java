package com.ashin.controller;

import com.ashin.entity.PageResult;
import com.ashin.entity.QueryPageBean;
import com.ashin.entity.Result;
import com.ashin.pojo.Goods;
import com.ashin.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @GetMapping("/findPage")
    public PageResult findPage(QueryPageBean queryPageBean){
        PageResult pageResult = goodsService.findPage(queryPageBean);
        return pageResult;
    }
    @GetMapping("/{id}")
    public Result<Goods> findById(@PathVariable Integer id){
        Goods goods = goodsService.findById(id);
        return new Result<Goods>(true,"查询成功", goods);
    }
    @PostMapping("/add")
    public Result add(@RequestBody Goods goods){
        goodsService.add(goods);
        return new Result(true,"新增成功");
    }
    @PutMapping("/up/{id}")
    public Result upGoods(@PathVariable Integer id) throws Exception {
        goodsService.upGoods(id);
        return new Result(true,"上架成功");
    }
    @PutMapping("/down/{id}")
    public Result downGoods(@PathVariable Integer id) throws Exception {
        goodsService.downGoods(id);
        return new Result(true,"下架成功");
    }
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        goodsService.delete(id);
        return new Result(true,"删除成功");
    }
    @PutMapping("/status/{id}/{status}")
    public Result delete(@PathVariable Integer id , @PathVariable String status){
        goodsService.updateStatus(id,status);
        return new Result(true,"操作成功");
    }
    @GetMapping("/findAll")
    public List<Goods> findAll(){
        List<Goods> goodsList = goodsService.findAll();
        return goodsList;
    }
    @PutMapping("/buy")
    public Result buy(@RequestParam(value = "goodId") Integer goodId,
                      @RequestParam(value = "buyNum") Integer buyNum){
        return goodsService.buy(goodId,buyNum);
    }
}