package com.ashin.search.controller;

import com.ashin.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/search")
//@CrossOrigin("*")//处理跨域问题
public class SearchController {
    @Autowired
    private SearchService searchService;

    @PostMapping
    public Map search(@RequestBody Map searchMap) throws IOException {
        Map resultMap = searchService.search(searchMap);
        return resultMap;
    }
}
