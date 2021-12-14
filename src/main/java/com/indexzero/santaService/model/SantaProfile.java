package com.indexzero.santaService.model;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
public class SantaProfile extends AbstractPersistable<Long> {
    
    private String santaProfileName;
    private String info;
    private int price;
    private boolean available;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "profile_image")
    private byte[] profileImage;

    //@JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "santaProfile", cascade = CascadeType.ALL)
    private List<UserAccount> users;

    @JsonBackReference
    @OneToMany(mappedBy = "santaProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Order> orders;
    
}
