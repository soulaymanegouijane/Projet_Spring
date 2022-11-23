package com.projet.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Association {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;
   private String name;
   private String description;

   @OneToMany(
           mappedBy = "association"
   )
    private Set<Member> members;

   @OneToMany(
           mappedBy = "association"
    )
    private Set<Offer> offers;

   @Override
    public String toString(){
       return "id :"+ id +
               "name : " +name;
   }
}
