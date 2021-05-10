package com.ashin.user.service.impl;

import com.ashin.pojo.Address;
import com.ashin.user.mapper.AddressMapper;
import com.ashin.user.service.AddressService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;
    @Override
    public List<Address> findByUserId(String username) {
        //根据用户名过滤
        //按照是否默认降序排序
        LambdaQueryWrapper<Address> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Address::getUserId, username);
        queryWrapper.orderByDesc(Address::getIsDefault);

        return addressMapper.selectList(queryWrapper);
    }
}
