package com.nephos.cb.service;

import com.nephos.cb.dao.BeerSampleRepository;
import com.nephos.cb.model.BeerSample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeerSampleService {

    @Autowired
    private BeerSampleRepository repo;

    public BeerSample findById(String id) {
        return repo.findById(id).get();
    }
}
