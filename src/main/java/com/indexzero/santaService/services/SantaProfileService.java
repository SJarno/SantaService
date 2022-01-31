package com.indexzero.santaService.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.indexzero.santaService.model.SantaProfile;
import com.indexzero.santaService.repositories.SantaProfileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SantaProfileService {

    @Autowired
    private SantaProfileRepository santaProfileRepository;

    @Autowired
    private SecurityContextService securityContextService;

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

    }

    /* Get available santas by city */
    public List<SantaProfile> getAvailableSantasByCity(String city) {
        return santaProfileRepository.customFindAllAvailableSantasByCity("%" + city + "%");
    }

    /* Get profile image by useraccount id */
    public byte[] getSantaprofileImage(Long id) {
        return santaProfileRepository.findById(id).get().getProfileImage();
    }

    /* Update profile */
    @Transactional
    public boolean updateSantaProfileInfo(
            SantaProfile updatedSantaProfile,
            MultipartFile image) throws IOException {

        SantaProfile existingSantaProfile = securityContextService.getAuthenticatedUserAccount().get()
                .getSantaProfile();
        /* Santaprofilename should not be empty: */
        if (updatedSantaProfile.getSantaProfileName().isBlank()) {
            throw new IllegalArgumentException("Profiilinimi liian lyhyt");
        }
        if (updatedSantaProfile.getCity().isBlank()) {
            throw new IllegalArgumentException("Toimipaikka liian lyhyt");
        }

        if (image.getSize() > 750000) {
            // more here:
            // https://stackoverflow.com/questions/27175729/get-correct-file-size-from-spring-multipartfile

            throw new IOException("Kuva on liian iso. Koko:" + image.getSize());
        }
        /* add new image only if exists */
        if (image.getBytes() != null && (image.getContentType().equals("image/png")
                || image.getContentType().equals("image/jpeg"))) {
            existingSantaProfile.setProfileImage(image.getBytes());

        }

        existingSantaProfile.setSantaProfileName(updatedSantaProfile.getSantaProfileName());
        existingSantaProfile.setInfo(updatedSantaProfile.getInfo());
        existingSantaProfile.setPrice(updatedSantaProfile.getPrice());
        existingSantaProfile.setCity(updatedSantaProfile.getCity());
        existingSantaProfile.setAvailable(updatedSantaProfile.isAvailable());
        existingSantaProfile.setContactEmail(updatedSantaProfile.getContactEmail());

        return true;

    }

    /* Delete Santaprofile */
    public void deleteSantaprofile(Long id) {
        santaProfileRepository.deleteById(id);
    }

}
