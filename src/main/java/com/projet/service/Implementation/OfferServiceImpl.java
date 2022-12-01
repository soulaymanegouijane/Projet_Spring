package com.projet.service.Implementation;

import com.projet.entity.Offer;
import com.projet.repository.OfferRepository;
import com.projet.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class OfferServiceImpl implements OfferService {

    @Autowired
    OfferRepository offerRepository;

    @Override
    @PreAuthorize("hasRole('MEMBER')")
    public Page<Offer> retreiveOffers(int page) {
        return offerRepository.findAll(PageRequest.of(page, 10));
    }

}
