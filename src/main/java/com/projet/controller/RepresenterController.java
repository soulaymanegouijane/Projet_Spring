package com.projet.controller;


import com.projet.entity.Offer;
import com.projet.service.RepresenterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RepresenterController {



//    @GetMapping("/offers")
//    public List<Offer> retreiveOffers(){
//        return representerService.retreiveAllAssociationOffers();
//    }

    @GetMapping
    public String retreive(){
        return "Hello";
    }
}
