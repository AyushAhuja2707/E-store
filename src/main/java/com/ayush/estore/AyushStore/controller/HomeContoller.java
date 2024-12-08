package com.ayush.estore.AyushStore.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/test")
public class HomeContoller {

    @GetMapping("")
    public String getMethodName() {
        return "Hi I am Aysuh";
    }

}
