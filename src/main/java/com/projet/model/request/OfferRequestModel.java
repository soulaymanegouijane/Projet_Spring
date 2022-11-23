package com.projet.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferRequestModel {
    private String title;
    private String description;
    private long materialId;
    private Set<Long> categoryIds;
}
