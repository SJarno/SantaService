package com.indexzero.santaService.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

    @ManyToOne
    private SantaProfile santaProfile;
    
    @ManyToOne
    private CustomerProfile customerProfile;
}
