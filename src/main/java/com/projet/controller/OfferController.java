package com.projet.controller;

import com.projet.entity.Offer;
import com.projet.repository.OfferRepository;
import com.projet.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/offers")
public class OfferController {
    @Autowired
    OfferService offerService;

    @GetMapping("/{page}")
    public Page<Offer> retreiveOffers(@PathVariable int page){
        return offerService.retreiveOffers(page);
    }
}
