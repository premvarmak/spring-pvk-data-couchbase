package com.nephos.cb.controller;

import com.nephos.cb.model.BeerSample;
import com.nephos.cb.service.BeerSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/beersample")
public class BeerSampleController {

    @Autowired
    private BeerSampleService service;

    @GetMapping("/{id}")
    public BeerSample getBeerSampleInfoById(@PathVariable String id) {
        return service.findById(id);
    }
}
