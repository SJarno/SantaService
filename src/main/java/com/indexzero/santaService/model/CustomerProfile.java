package com.indexzero.santaService.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
    
    private String customerProfileName;

    @JsonBackReference
    @OneToMany(mappedBy = "customerProfile", cascade = CascadeType.ALL)
    private List<UserAccount> users;

    @JsonBackReference
    @OneToMany(mappedBy = "customerProfile",cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Order> orders;
    

}
