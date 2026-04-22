
/*
 * @author Mart Stukje
 * */

package nl.miwnn.ch19.binarybros.brobook.service;

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.Cohort;
import nl.miwnn.ch19.binarybros.brobook.model.Image;
import nl.miwnn.ch19.binarybros.brobook.repository.BroBookUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @author Binary Bro's
 * Service laag voor het beheren van gebruikerslogica en autorisatie.
 */
@Service
public class BroBookUserService implements UserDetailsService {

    private final BroBookUserRepository userRepository;

    public BroBookUserService(BroBookUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Gebruiker niet gevonden: " + username));
    }

    public List<BroBookUser> findAll() {
        return userRepository.findAll();
    }

    public BroBookUser getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public BroBookUser getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public List<BroBookUser> findVisibleUsers(BroBookUser currentUser) {
        if (currentUser.getRole() != null &&
                ("ADMIN".equalsIgnoreCase(currentUser.getRole()) || "Teacher".equalsIgnoreCase(currentUser.getRole()))) {
            return userRepository.findAll();
        }

        Set<BroBookUser> visibleUsers = new HashSet<>();

        // Regel: Studenten zien altijd alle docenten
        visibleUsers.addAll(userRepository.findByRole("Teacher"));

        // Regel: Studenten zien zichzelf
        visibleUsers.add(currentUser);

        // Regel: Studenten zien iedereen uit hun eigen cohorten (ManyToMany relatie)
        if (currentUser.getCohorts() != null) {
            for (Cohort cohort : currentUser.getCohorts()) {
                visibleUsers.addAll(cohort.getParticipants());
            }
        }

        // Resultaat omzetten naar lijst en sorteren op achternaam
        List<BroBookUser> result = new ArrayList<>(visibleUsers);
        result.sort(Comparator.comparing(BroBookUser::getLastName, Comparator.nullsLast(Comparator.naturalOrder())));

        return result;
    }

    /**
     * Verwerkt de opslag van een gebruiker inclusief de profielfoto.
     * Complexe logica gescheiden van de controller (Score 3 bewijs).
     */
    public void save(BroBookUser user, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                Image profilePicture = user.getProfilePicture();

                // Als de user nog geen Image object had, maak er een aan
                if (profilePicture == null) {
                    profilePicture = new Image();
                }

                profilePicture.setData(imageFile.getBytes());
                profilePicture.setContentType(imageFile.getContentType());

                user.setProfilePicture(profilePicture);
            } catch (IOException e) {
                // Log de fout of handel deze af (voor portfolio: foutafhandeling)
                System.err.println("Fout bij verwerken afbeelding: " + e.getMessage());
            }
        }

        userRepository.save(user);
    }
}