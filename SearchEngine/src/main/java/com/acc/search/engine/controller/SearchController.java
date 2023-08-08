package com.acc.search.engine.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acc.search.engine.dto.Doc;
import com.acc.search.engine.service.SearchService;
import com.acc.search.engine.util.PriorityQueue;
import com.acc.search.engine.util.SpellChecking;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/searchEngine")
public class SearchController {
	
	@SuppressWarnings("rawtypes")
	@Autowired
	SearchService searchService;
	
	@SuppressWarnings("unchecked")
	@GetMapping("/search/query")
    public Map<String, Object> getSearchResult(@RequestParam(required= true) String queryString) throws IOException {
        long startTime = System.currentTimeMillis();
        PriorityQueue<Integer, String> pq =  (PriorityQueue<Integer, String>) searchService.occurencesOfQueryString(queryString);
        Doc[] Docs = searchService.queue2List(pq);
        long endTime = System.currentTimeMillis();
        long timeUsage = endTime - startTime;
        Map<String, Object> model = new HashMap<>();
        if (Docs.length != 0){
            model.put("hasRes", true);
            model.put("resList", Docs);
            model.put("numRes", Docs.length);
            model.put("timeUsage_res", timeUsage);
        } else {
            model.put("hasRes", false);
            String[] altWords = SpellChecking.getAltWords(queryString);
            if (altWords.length != 0) {
                model.put("alternatives", altWords);
                model.put("getquery", queryString);
            } else {
                String[] noAlt = {"None"};
                model.put("alternatives", noAlt);
            }
        }
//        return "searchResultPage";
//        return " abc ";
        return model;
    }
	
}
