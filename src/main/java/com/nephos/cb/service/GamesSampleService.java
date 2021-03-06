package com.nephos.cb.service;

import com.nephos.cb.dao.GamesSampleRepository;
import com.nephos.cb.model.GamesSample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GamesSampleService {

    @Autowired
    private GamesSampleRepository repo;

    public GamesSample findByGameId(String id) {
        return repo.findById(id).get();
    }
}
