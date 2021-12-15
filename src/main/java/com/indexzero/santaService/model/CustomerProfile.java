package com.indexzero.santaService.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.AbstractPersistable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerProfile extends AbstractPersistable<Long>{
    
    @Column(name = "customer_profile_name")
    private String customerProfileName;

    @Column(name = "contact_email")
    @Email
    private String email;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "postal_code")
    private String postalCode;

    //@JsonBackReference
    @JsonIgnore
    @OneToMany(mappedBy = "customerProfile", cascade = CascadeType.ALL)
    private List<UserAccount> users;

    @JsonBackReference
    @OneToMany(mappedBy = "customerProfile",cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Order> orders;
    

}
