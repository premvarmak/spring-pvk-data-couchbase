package com.nephos.cb.controller;

import com.nephos.cb.model.TravelSample;
import com.nephos.cb.service.TravelSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/travelsample")
public class TravelSampleController {

    @Autowired
    private TravelSampleService service;

    @GetMapping("/{id}")
    public TravelSample getAirLineById(@PathVariable String id) {
        return service.findAirlineById(id);
    }

}
