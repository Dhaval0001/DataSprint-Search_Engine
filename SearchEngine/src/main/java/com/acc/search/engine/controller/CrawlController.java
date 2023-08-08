package com.acc.search.engine.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acc.search.engine.service.CrawlerService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/crawler")
public class CrawlController {
	
	@Autowired
	CrawlerService crawlerService;
	
	@RequestMapping("/crawler")
    public Map<String, Object> getURL(@RequestParam("urlstr") String url){
        Long startTime = System.currentTimeMillis();
        Long endTime = System.currentTimeMillis();
        String[] urlList = crawlerService.getURLList(url);
        Long time = endTime - startTime;
        Map<String, Object> model = new HashMap<>();
        model.put("urlList", urlList);
        model.put("numURLs", urlList.length);
        model.put("timeUsage_url", time);
        return model;
	}
    

}
