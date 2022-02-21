package com.nephos.cb.service;

import com.nephos.cb.dao.TravelSampleRepository;
import com.nephos.cb.model.TravelSample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TravelSampleService {

    @Autowired
    private TravelSampleRepository repo;

    public TravelSample findAirlineById(String id) {
        return repo.findById(id).get();
    }
}
