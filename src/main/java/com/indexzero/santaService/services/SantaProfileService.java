package com.indexzero.santaService.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.indexzero.santaService.model.SantaProfile;
import com.indexzero.santaService.model.UserAccount;
import com.indexzero.santaService.repositories.SantaProfileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SantaProfileService {

    @Autowired
    private SantaProfileRepository santaProfileRepository;

    @Autowired
    private UserAccountService userAccountService;


    @Transactional
    public void saveSantaProfile(SantaProfile santaProfile) {
        santaProfileRepository.saveAndFlush(santaProfile);
    }

    public Optional<SantaProfile> getProfileByid(Long id) {
        return santaProfileRepository.findById(id);
    }

    public List<SantaProfile> getAllSantas() {
        return santaProfileRepository.findAll();
    }

    /* Get available santas: */
    public List<SantaProfile> getAvailableSantas() {
        return santaProfileRepository.customFindAllAvailableSantas();
        /* return convertDataFromList(santaProfileRepository.customFindAllAvailableSantas()); */
        /* UserAccount userAccount = userAccountService.findUserAccountByUsername(getAuthenticatedUser().getName()).get();
        Long customerId = userAccount.getCustomerProfile().getId();
        return santaProfileRepository.customFindAllAvailableSantasWhereCustomerNotApplied(customerId); */
    }

    /* Availabel santas by postalcode */
    public List<SantaProfile> getAvailableSantasByPostalCode(String postalCode) {
        return convertDataFromList(santaProfileRepository.customFindAllAvailableSantasByPostalCode(postalCode));
    }

    /* Get profile image by useraccount id*/
    public byte[] getSantaprofileImage(Long id) {
        /* UserAccount userAccount = userAccountService.findUserAccountById(id).get();
        return userAccount.getSantaProfile().getProfileImage(); */
        return santaProfileRepository.findById(id).get().getProfileImage();
    }

    /* Update profile */
    @Transactional
    public void updateSantaProfileInfo(
            UserAccount userAccount,
            SantaProfile existingSantaProfile,
            SantaProfile updatedSantaProfile) {

        /* Santaprofile should not be empty: */
        
        if (!updatedSantaProfile.getSantaProfileName().isBlank()) {
            existingSantaProfile.setSantaProfileName(updatedSantaProfile.getSantaProfileName());
        }
        /* add new image only if exists */
        if (updatedSantaProfile.getProfileImage() != null) {
            existingSantaProfile.setProfileImage(updatedSantaProfile.getProfileImage());
        }
        existingSantaProfile.setInfo(updatedSantaProfile.getInfo());
        existingSantaProfile.setPrice(updatedSantaProfile.getPrice());
        existingSantaProfile.setAvailable(updatedSantaProfile.isAvailable());

    }

    /* Delete Santaprofile */
    public void deleteSantaprofile(Long id) {
        santaProfileRepository.deleteById(id);
    }

    /* converts data only needed info */
    private List<SantaProfile> convertDataFromList(List<SantaProfile> list) {
        return list.stream()
                .map(santa -> {
                    SantaProfile santaProfile = new SantaProfile();
                    santaProfile.setSantaProfileName(santa.getSantaProfileName());
                    santaProfile.setProfileImage(santa.getProfileImage());
                    santaProfile.setInfo(santa.getInfo());
                    santaProfile.setPrice(santa.getPrice());
                    santaProfile.setAvailable(santa.isAvailable());
                    return santaProfile;
                }).collect(Collectors.toList());
    }
    /* filters available santas: */
    private List<SantaProfile> convertDataByAvailable(List<SantaProfile> list) {
        return list.stream()
                .filter(santa -> santa.isAvailable())
                .collect(Collectors.toList());
    }
    private Authentication getAuthenticatedUser() {
        return SecurityContextHolder.getContext().getAuthentication();

    }

}
