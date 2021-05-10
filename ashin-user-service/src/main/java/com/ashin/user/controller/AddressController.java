package com.ashin.user.controller;

import com.ashin.pojo.Address;
import com.ashin.user.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping("{username}")
    public List<Address> findByUserId(@PathVariable String username) {
        return addressService.findByUserId(username);
    }
}
