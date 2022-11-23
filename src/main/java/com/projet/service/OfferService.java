package com.projet.service;

import com.projet.entity.Offer;
import org.springframework.data.domain.Page;

public interface OfferService {
    Page<Offer> retreiveOffers(int page);
}
