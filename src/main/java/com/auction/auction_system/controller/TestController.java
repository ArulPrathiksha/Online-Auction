package com.auction.auction_system.controller;

import com.auction.auction_system.entity.User;
import com.auction.auction_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/test")
    public String test() {
        User u = new User();
        u.setName("Test User");
        u.setEmail("test@gmail.com");
        userRepo.save(u);
        return "Added Successfully!";
    }
}

