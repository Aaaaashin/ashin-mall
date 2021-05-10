package com.ashin.order.feign;

import com.ashin.entity.Result;
import com.ashin.pojo.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "goods-service")
public interface GoodsFeign {
    @GetMapping("goods/{id}")
    Result<Goods> findByGoodId(@PathVariable Integer id);

    @PutMapping("goods/buy")
    Result buy(@RequestParam Integer goodId, @RequestParam Integer buyNum);
}
