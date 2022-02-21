package com.nephos.cb.controller;

import com.nephos.cb.model.GamesSample;
import com.nephos.cb.service.GamesSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/gamessample")
public class GamesSampleController {

    @Autowired
    private GamesSampleService service;

    @GetMapping("/{id}")
    public GamesSample findGameById(@PathVariable String id) {
        return service.findByGameId(id);
    }
}
