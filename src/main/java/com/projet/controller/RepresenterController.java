package com.projet.controller;


import com.projet.entity.Demand;
import com.projet.entity.Offer;
import com.projet.exception.CategoryNotFoundException;
import com.projet.exception.MaterialNotFoundException;
import com.projet.exception.OfferNotFoundException;
import com.projet.model.request.DemandDecisionModel;
import com.projet.model.request.OfferRequestModel;
import com.projet.model.response.MessageResponse;
import com.projet.model.response.ResponseMessage;
import com.projet.service.RepresenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/representer")
public class RepresenterController {

    @Autowired
    RepresenterService representerService;


    @PostMapping("/offers")
    public ResponseEntity<ResponseMessage> addOffer(@RequestBody OfferRequestModel offer) throws MaterialNotFoundException, CategoryNotFoundException {
        return representerService.persistOffer(offer);
    }

    @GetMapping("/offers")
    public Set<Offer> getAllOffers() {
        return representerService.retreiveAllAssociationOffers();
    }

    @PutMapping("/demands/{id}")
    public ResponseEntity<ResponseMessage> acceptOrRefuseDemand(@PathVariable Long id, @RequestBody DemandDecisionModel demandDecision) throws MaterialNotFoundException, CategoryNotFoundException {
        return representerService.acceptOrRefuseDemand(id, demandDecision);
    }

    @GetMapping("/offer/{id}/demands")
    public Set<Demand> getAllDemands(@PathVariable long id) throws OfferNotFoundException {
        return representerService.retreiveAllAssociationDemands(id);
    }

    @PutMapping("/demands/{id}")
    public ResponseEntity<MessageResponse> transferDemand(@PathVariable long id){
        return representerService.transferDemand(id);

    }



}
