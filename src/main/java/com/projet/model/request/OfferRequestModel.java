package com.projet.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferRequestModel {
    private String title;
    private String description;
    private MultipartFile image;
    private Set<Long> categoryIds;
}
