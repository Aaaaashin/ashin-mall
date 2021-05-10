package com.ashin.user.service;

import com.ashin.pojo.Address;

import java.util.List;

public interface AddressService {
    List<Address> findByUserId(String username);
}
