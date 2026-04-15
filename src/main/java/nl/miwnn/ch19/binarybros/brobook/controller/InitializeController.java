package nl.miwnn.ch19.binarybros.brobook.controller;

/*
 * @author Mart Stukje
 * */

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.repository.BroBookUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

@Controller
public class InitializeController {

    private final BroBookUserRepository broBookUserRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(InitializeController.class);

    public InitializeController(BroBookUserRepository broBookUserRepository,
                                PasswordEncoder passwordEncoder) {
        this.broBookUserRepository = broBookUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void seed() {
        if (broBookUserRepository.count() == 0) {
            userSeed();
        }
    }

    private void userSeed() {
        BroBookUser beheerder = new BroBookUser(
                "De", "Beheerder",
                passwordEncoder.encode("beheerder"), "ADMIN");
        broBookUserRepository.save(beheerder);
    }
}
