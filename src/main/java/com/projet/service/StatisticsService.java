package com.projet.service;

import com.projet.model.response.Statistics;
import org.springframework.http.ResponseEntity;

public interface StatisticsService {
    ResponseEntity<com.projet.model.response.Statistics> retreiveDemandStatistics();

    ResponseEntity<Statistics> retreiveOfferStatistics();
}
