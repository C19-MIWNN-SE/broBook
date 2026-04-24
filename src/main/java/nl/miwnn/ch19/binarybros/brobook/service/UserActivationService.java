package nl.miwnn.ch19.binarybros.brobook.service;

/*
 * @author Mart Stukje
 * Handles all business logic regarding user activation
 * */

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.UserActivation;
import nl.miwnn.ch19.binarybros.brobook.repository.BroBookUserRepository;
import nl.miwnn.ch19.binarybros.brobook.repository.UserActivationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserActivationService {

    private static final int TOKEN_LENGTH = 8;
    private static final String USED_CHARACTERS = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private static final Logger log = LoggerFactory.getLogger(UserActivationService.class);

    private final UserActivationRepository userActivationRepository;
    private final PasswordEncoder passwordEncoder;
    private final BroBookUserRepository broBookUserRepository;

    public UserActivationService(UserActivationRepository userActivationRepository,
                                 PasswordEncoder passwordEncoder,
                                 BroBookUserRepository broBookUserRepository) {
        this.userActivationRepository = userActivationRepository;
        this.passwordEncoder = passwordEncoder;
        this.broBookUserRepository = broBookUserRepository;
    }

    public List<UserActivation> findAllById(List<Long> ids) {
        List<UserActivation> activations = userActivationRepository.findAllByUserIdIn(ids);
        log.debug("Gebruikeractivaties gevonden: {}", activations.size());
        return activations;
    }

    public boolean tokenIsActive(String token) {
        return userActivationRepository.existsUserActivationByTokenAndUsedIsFalse(token);
    }

    public UserActivation generateActivation(BroBookUser user) {
        String token = generateUniqueToken();

        UserActivation activation = new UserActivation();
        activation.setUser(user);
        activation.setToken(token);
        activation.setExpireDate(LocalDate.now().plusDays(7));

        return userActivationRepository.save(activation);
    }

    private String generateUniqueToken() {
        String token;
        do {
            token = generateToken();
        } while (userActivationRepository.existsByToken(token));
        return token;
    }

    private String generateToken() {
        StringBuilder builder = new StringBuilder(TOKEN_LENGTH);
        for (int character = 0; character < TOKEN_LENGTH; character++) {
            builder.append(USED_CHARACTERS.charAt(RANDOM.nextInt(USED_CHARACTERS.length())));
        }
        return builder.toString();
    }

    public void savePasswordWithToken(String password, String token) {
        UserActivation activation = userActivationRepository.findByToken(token);
        BroBookUser user = activation.getUser();

        user.setPassword(passwordEncoder.encode(password));
        broBookUserRepository.save(user);
    }

}
