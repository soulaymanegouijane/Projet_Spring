package com.projet.service.Implementation;

import com.projet.entity.Offer;
import com.projet.repository.OfferRepository;
import com.projet.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    @Autowired
    OfferRepository offerRepository;

    @Override
//    @PreAuthorize("hasRole('MEMBER')")
    public List<Offer> retreiveOffers() {
        return offerRepository.findAll();
    }

}
