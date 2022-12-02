package com.projet.service.Implementation;

import com.projet.entity.*;
import com.projet.exception.CategoryNotFoundException;
import com.projet.exception.DemandNotFoundException;
import com.projet.exception.MaterialNotFoundException;
import com.projet.exception.OfferNotFoundException;
import com.projet.model.request.OfferRequestModel;
import com.projet.model.response.MessageResponse;
import com.projet.model.response.ResponseMessage;
import com.projet.repository.CategoryRepository;
import com.projet.repository.DemandRepository;
import com.projet.repository.MemberRepository;
import com.projet.repository.OfferRepository;
import com.projet.security.services.UserDetailsImpl;
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
import java.util.List;
import java.util.Set;

@Service
public class RepresenterServiceImpl implements RepresenterService {

    @Autowired
    OfferRepository offerRepository;

    @Autowired
    MemberRepository memberRepository;


    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    DemandRepository demandRepository;

    @Override
    @PreAuthorize("hasRole('REPRESENTATIVE')")
    public List<Offer> retreiveAllAssociationOffers() {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return offerRepository.findByAssociationId(member.getAssociation().getId());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('REPRESENTATIVE')")
    public ResponseEntity<ResponseMessage> persistOffer(OfferRequestModel offer) throws MaterialNotFoundException, CategoryNotFoundException {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("L'utilisateur avec l'id " + userDetails.getId() + " n'existe pas"));
        Set<Category> categories = new HashSet<>();
        for (long categoryId : offer.getCategoryIds()) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CategoryNotFoundException("La Catégorie avec l'id " + categoryId + " n'existe pas"));
            categories.add(category);
        }
        Offer newOffer = Offer.builder()
                .categories(categories)
                .description(offer.getDescription())
                .title(offer.getTitle())
                .association(member.getAssociation())
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
    public ResponseEntity<ResponseMessage> RefuseDemand(Long id, String comment) {
        Demand demand = demandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La demande avec l'id " + id + " n'existe pas"));
            if (comment == null || comment.isEmpty()) {
                return ResponseEntity.badRequest().body(ResponseMessage.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Vous devez ajouter un commentaire")
                        .build());
            }
            demand.setStatus(Decision.REFUSED);
            demand.setComment(comment);
        demandRepository.save(demand);
        return ResponseEntity.ok()
                .body(ResponseMessage.builder()
                        .status(HttpStatus.ACCEPTED)
                        .message("Votre demande a été mis à jour")
                        .build());
    }

    @Override
    @PreAuthorize("hasRole('REPRESENTATIVE')")
    public Set<Demand> retreiveAllAssociationDemands() throws OfferNotFoundException {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("L'utilisateur avec l'id " + userDetails.getId() + " n'existe pas"));
        List<Offer> offers = offerRepository.findByAssociationId(member.getAssociation().getId());
        System.out.println(offers);
        return offers.stream()
                .map(Offer::getDemands)
                .flatMap(Set::stream)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);
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

    @Override
    @PreAuthorize("hasRole('REPRESENTATIVE')")
    public ResponseEntity<ResponseMessage> acceptDemand(Long id) {
        Demand demand = demandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La demande avec l'id " + id + " n'existe pas"));
        demand.setStatus(Decision.ACCEPTED);
        demandRepository.save(demand);
        return ResponseEntity.ok()
                .body(ResponseMessage.builder()
                        .status(HttpStatus.ACCEPTED)
                        .message("Votre demande a été mis à jour")
                        .build());
    }
}
