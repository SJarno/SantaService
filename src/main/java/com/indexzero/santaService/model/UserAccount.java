package com.indexzero.santaService.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
public class UserAccount extends AbstractPersistable<Long> {
    /* Security */

    @NotBlank(message = "Käyttäjätunnus ei saa olla tyhjä")
    private String username;

    @NotBlank(message = "Salasana ei saa olla tyhjä")
    @Size(min = 3, message = "Salasanan täytyy olla vähintään kolme merkkinen")
    private String password;
    private String userRole;

    /* Basic contact information */
    @Column(name = "first_name")
    @NotBlank(message = "Kenttä ei saa olla tyhjä")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Kenttä ei saa olla tyhjä")
    private String lastName;

    @NotBlank(message = "Kenttä ei saa olla tyhjä")
    private String email;

    @Column(name = "phone_number")
    @NotBlank(message = "Kenttä ei saa olla tyhjä")
    private String phoneNumber;

    @NotBlank(message = "Kenttä ei saa olla tyhjä")
    private String address;

    @Column(name = "postal_code")
    @NotBlank(message = "Kenttä ei saa olla tyhjä")
    private String postalCode;

    /*Santa Profile, get json back */
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SantaProfile santaProfile;
    
    /* Customer profile, get json back */
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CustomerProfile customerProfile;

    public Boolean anyValueBlank() {
        if (this.firstName.isBlank()) return true;
        if (this.lastName.isBlank()) return true;
        if (this.email.isBlank()) return true;
        if (this.phoneNumber.isBlank()) return true;
        if (this.postalCode.isBlank()) return true;
        return false;
    }
    

}
