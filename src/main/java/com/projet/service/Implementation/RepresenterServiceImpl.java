package com.projet.service.Implementation;

import com.projet.entity.*;
import com.projet.exception.CategoryNotFoundException;
import com.projet.exception.DemandNotFoundException;
import com.projet.exception.MaterialNotFoundException;
import com.projet.exception.OfferNotFoundException;
import com.projet.model.request.DemandDecisionModel;
import com.projet.model.request.OfferRequestModel;
import com.projet.model.response.MessageResponse;
import com.projet.model.response.ResponseMessage;
import com.projet.repository.CategoryRepository;
import com.projet.repository.DemandRepository;
import com.projet.repository.MaterialRepository;
import com.projet.repository.OfferRepository;
import com.projet.service.RepresenterService;
import com.projet.utils.Decision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class RepresenterServiceImpl implements RepresenterService {

    @Autowired
    OfferRepository offerRepository;

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    DemandRepository demandRepository;

    @Override
    @PreAuthorize("hasRole('REPRESENTATIVE')")
    public Set<Offer> retreiveAllAssociationOffers() {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return offerRepository.findByAssociationId(member.getAssociation().getId());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('REPRESENTATIVE')")
    public ResponseEntity<ResponseMessage> persistOffer(OfferRequestModel offer) throws MaterialNotFoundException, CategoryNotFoundException {
        if (offerRepository.existsByMaterialId(offer.getMaterialId())) {
            return ResponseEntity.badRequest().body(ResponseMessage.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Le produit que vous avez selectionné contient déjà une offre")
                    .build());
        }
        Set<Category> categories = new HashSet<>();
        Material material = materialRepository.findById(offer.getMaterialId())
                .orElseThrow(() -> new MaterialNotFoundException("le material avec l'id " + offer.getMaterialId() + " n'existe pas"));
        for (long categoryId : offer.getCategoryIds()) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CategoryNotFoundException("La Catégorie avec l'id " + categoryId + " n'existe pas"));
            categories.add(category);
        }
        Offer newOffer = Offer.builder()
                .material(material)
                .categories(categories)
                .description(offer.getDescription())
                .title(offer.getTitle())
                .build();
        offerRepository.save(newOffer);
        return ResponseEntity.ok(ResponseMessage.builder()
                .status(HttpStatus.OK)
                .message("L'offre a été ajoutée avec succès")
                .build());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('REPRESENTATIVE')")
    public ResponseEntity<ResponseMessage> acceptOrRefuseDemand(Long id, DemandDecisionModel demandDecision) {
        Demand demand = demandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La demande avec l'id " + id + " n'existe pas"));
        if(demandDecision.isAccepted()){
            demand.setStatus(Decision.ACCEPTED);
        }else{
            if (demandDecision.getComment() == null || demandDecision.getComment().isEmpty()) {
                return ResponseEntity.badRequest().body(ResponseMessage.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Vous devez ajouter un commentaire")
                        .build());
            }
            demand.setStatus(Decision.REFUSED);
            demand.setComment(demandDecision.getComment());
        }
        demandRepository.save(demand);
        return ResponseEntity.ok()
                .body(ResponseMessage.builder()
                        .status(HttpStatus.ACCEPTED)
                        .message("Votre demande a été mis à jour")
                        .build());
    }

    @Override
    @PreAuthorize("hasRole('REPRESENTATIVE')")
    public Set<Demand> retreiveAllAssociationDemands(long id) throws OfferNotFoundException {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Offer> offers = offerRepository.findByAssociationId(member.getAssociation().getId());
        return offers.stream()
                .filter(offer -> offer.getId() == id)
                .findFirst()
                .orElseThrow(() -> new OfferNotFoundException("L'offre avec l'id " + id + " n'existe pas"))
                .getDemands();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('REPRESENTATIVE')")
    public ResponseEntity<MessageResponse> transferDemand(long id) throws DemandNotFoundException {
        Demand demand = demandRepository.findById(id)
                .orElseThrow(()-> new DemandNotFoundException("La demande avec l'id "+id+" n'existe pas"));
        Offer offer = demand.getOffer();
        demand.setStatus(Decision.TRANSEFERED);
        demand.setArchived(true);
        offer.setArchived(true);
        offerRepository.save(offer);
        demandRepository.save(demand);
        return ResponseEntity.ok()
                .body(MessageResponse.builder()
                        .status(HttpStatus.OK)
                        .message("La demande a été transférée avec succès")
                        .build());
    }
}
