package com.ashin.sync.controller;

import com.ashin.entity.Result;
import com.ashin.sync.service.DataSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/datasync")
public class DataSyncController {

    @Autowired
    private DataSyncService dataSyncService;

    @GetMapping("/importAll")
    public Result importAll(){
        dataSyncService.importAll();
        return new Result(true,"操作成功");
    }
}