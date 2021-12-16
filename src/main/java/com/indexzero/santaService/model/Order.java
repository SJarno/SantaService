package com.indexzero.santaService.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.springframework.data.jpa.domain.AbstractPersistable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "Orders")
public class Order extends AbstractPersistable<Long>{
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /* Poistoon osoitteet ja postikoodit?: */
    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "postal_code")
    private String postalCode;

    //@JsonBackReference
    @JsonManagedReference //palauttaa santaprofiilin
    @ManyToOne
    private SantaProfile santaProfile;
    
    //@JsonBackReference 
    @JsonManagedReference //palauttaa asiakasprofiilin
    @ManyToOne
    private CustomerProfile customerProfile;
}
