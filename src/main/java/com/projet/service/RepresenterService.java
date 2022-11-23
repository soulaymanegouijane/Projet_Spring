package com.projet.service;

import com.projet.entity.Demand;
import com.projet.entity.Offer;
import com.projet.exception.CategoryNotFoundException;
import com.projet.exception.DemandNotFoundException;
import com.projet.exception.MaterialNotFoundException;
import com.projet.exception.OfferNotFoundException;
import com.projet.model.request.DemandDecisionModel;
import com.projet.model.request.OfferRequestModel;
import com.projet.model.response.MessageResponse;
import com.projet.model.response.ResponseMessage;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface RepresenterService {
    Set<Offer> retreiveAllAssociationOffers();

    ResponseEntity<ResponseMessage> persistOffer(OfferRequestModel offer) throws MaterialNotFoundException, CategoryNotFoundException;

    ResponseEntity<ResponseMessage> acceptOrRefuseDemand(Long id, DemandDecisionModel demandDecision);

    Set<Demand> retreiveAllAssociationDemands(long id) throws OfferNotFoundException;

    ResponseEntity<MessageResponse> transferDemand(long id) throws DemandNotFoundException;
}
