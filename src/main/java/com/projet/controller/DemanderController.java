package com.projet.controller;

import com.projet.entity.Offer;
import com.projet.exception.MemberNotFoundException;
import com.projet.exception.OfferNotFoundException;
import com.projet.model.request.DemandRequestModel;
import com.projet.model.response.ResponseMessage;
import com.projet.repository.DemandRepository;
import com.projet.service.DemanderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/demander")
@CrossOrigin("*")
public class DemanderController {
    @Autowired
    DemanderService demandService;


    @GetMapping("/offers/{id}")
    public Offer retreiveOfferById(@PathVariable Long id) throws OfferNotFoundException {
        return demandService.getOfferById(id);
    }

    @PostMapping("/demands")
    public ResponseEntity<ResponseMessage> createDemand(@RequestBody DemandRequestModel demand) throws MemberNotFoundException, OfferNotFoundException {
        return demandService.persistDemand(demand);
    }

}
