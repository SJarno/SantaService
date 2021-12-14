package com.indexzero.santaService.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.AbstractPersistable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "Orders")
public class Order extends AbstractPersistable<Long>{
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //@JsonBackReference
    @JsonManagedReference //palauttaa santaprofiilin
    @ManyToOne
    private SantaProfile santaProfile;
    
    @JsonBackReference //ei palauta asiakasprofiilia atm
    @ManyToOne
    private CustomerProfile customerProfile;
}
