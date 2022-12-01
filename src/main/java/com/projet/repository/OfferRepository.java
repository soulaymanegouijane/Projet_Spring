package com.projet.repository;

import com.projet.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    @Query("SELECT o FROM Offer o WHERE o.association.id = ?1")
    Set<Offer> findByAssociationId(long id);

    @Query("SELECT count(o) FROM Offer o WHERE o.association.id = ?1")
    Long countByAssociationId(long id);

    @Query("SELECT count(o) FROM Offer o WHERE o.association.id = ?1 and o.isArchived = ?2")
    Long countByAssociationIdAndArchived(long id, boolean isArchived);


}
