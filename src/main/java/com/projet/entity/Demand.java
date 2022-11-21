package com.projet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Demand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreationTimestamp
    private Date creationDate;

    private boolean isArchived;

    private int rank;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Offer offer;

    public void reduceRank(){
        this.rank = this.rank - 1;
    }
}
