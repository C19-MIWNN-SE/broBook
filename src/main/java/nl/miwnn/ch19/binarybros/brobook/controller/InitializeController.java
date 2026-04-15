package nl.miwnn.ch19.binarybros.brobook.controller;

/*
 * @author Binary Bro's
 * */

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.Cohort;
import nl.miwnn.ch19.binarybros.brobook.repository.BroBookUserRepository;
import nl.miwnn.ch19.binarybros.brobook.repository.CohortRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

@Controller
public class InitializeController {

    private final BroBookUserRepository broBookUserRepository;
    private final CohortRepository cohortRepository;
    private final PasswordEncoder passwordEncoder;


    private static final Logger log = LoggerFactory.getLogger(InitializeController.class);

    public InitializeController(BroBookUserRepository broBookUserRepository, CohortRepository cohortRepository,
                                PasswordEncoder passwordEncoder) {
        this.broBookUserRepository = broBookUserRepository;
        this.cohortRepository = cohortRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void seed() {
        if (cohortRepository.count() == 0) {
            cohortSeed();
        }
        if (broBookUserRepository.count() == 0) {
            userSeed();
            studentSeed();
            docentSeed();
        }
    }

    private void userSeed() {
        BroBookUser beheerder = new BroBookUser(
                "De", "Beheerder",
                passwordEncoder.encode("beheerder"), "ADMIN");
        broBookUserRepository.save(beheerder);
    }

    private void studentSeed() {
        broBookUserRepository.save(new BroBookUser(
                "Paul", "Rademaker",
                "IBM", LocalDate.parse("1991-04-03"), "Student"));
        broBookUserRepository.save(new BroBookUser(
                "Mart", "Stukje",
                "Univé", LocalDate.parse("1997-01-01"), "Student"));
        broBookUserRepository.save(new BroBookUser(
                "Jan", "Jansen",
                "Sopra Steria", LocalDate.parse("1995-01-01"), "Student"));
        broBookUserRepository.save(new BroBookUser(
                "Peter", "de Jong",
                "De ree", LocalDate.parse("2001-01-01"), "Student"));

        log.info("Test Studenten aangemaakt.");
    }

    private void docentSeed() {
        broBookUserRepository.save(new BroBookUser("Vincent", "Velthuizen", "Docent"));
        broBookUserRepository.save(new BroBookUser("Arjen", "Loermans", "Docent"));
    }

    private void cohortSeed() {
        cohortRepository.save(new Cohort("Cohort 1", "Software Engineering"));
        cohortRepository.save(new Cohort("Cohort 2", "Functioneel Beheer"));
        cohortRepository.save(new Cohort("Cohort 3", "Software Engineering"));
        cohortRepository.save(new Cohort("Cohort 4", "Functioneel Beheer"));
        cohortRepository.save(new Cohort("Cohort 5", "Software Engineering"));
        cohortRepository.save(new Cohort("Cohort 6", "Functioneel Beheer"));

    }
}
