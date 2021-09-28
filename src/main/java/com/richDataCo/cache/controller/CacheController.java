package com.richDataCo.cache.controller;

import com.richDataCo.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CacheController {

    private final CacheService cacheService;

    @Autowired
    public CacheController(CacheService cacheService){
        this.cacheService = cacheService;
    }
    @GetMapping(path = "api/")
    public String hello() {
        return cacheService.hello();
    }

    @GetMapping(path = "api/get")
    public String getDataByKey(@RequestParam String key) {
        return cacheService.getData(key);
    }

    @PostMapping(path = "api/storeData")
    public void storeCache(@RequestBody String data){
        cacheService.storeData(data);
    }
}
