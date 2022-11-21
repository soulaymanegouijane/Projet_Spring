package com.projet.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Offer {
    @Id
    private long id;

    private String title;

    private String description;

    @ManyToMany
    Set<Category> categories;

    @OneToMany
    private Set<Demand> demands;

    @OneToOne
    private Material material;
}
