package com.ashin.order.feign;

import com.ashin.pojo.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "user-service")
public interface UserFeign {
    @GetMapping("address/{username}")
    List<Address> findByUserId(@PathVariable String username);
}
