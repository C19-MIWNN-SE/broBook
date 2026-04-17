package nl.miwnn.ch19.binarybros.brobook.controller;

/*
 * @author Binary Bro's
 * */

import com.opencsv.bean.CsvToBeanBuilder;
import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.Cohort;
import nl.miwnn.ch19.binarybros.brobook.model.Image;
import nl.miwnn.ch19.binarybros.brobook.repository.BroBookUserRepository;
import nl.miwnn.ch19.binarybros.brobook.repository.CohortRepository;
import nl.miwnn.ch19.binarybros.brobook.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Controller
public class InitializeController {

    private final BroBookUserRepository broBookUserRepository;
    private final CohortRepository cohortRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final Random random = new Random();

    @Value("${brobook.seed.default.password:default}")
    private String defaultPassword;


    private static final Logger log = LoggerFactory.getLogger(InitializeController.class);

    public InitializeController(BroBookUserRepository broBookUserRepository,
                                CohortRepository cohortRepository,
                                PasswordEncoder passwordEncoder,
                                ImageService imageService) {
        this.broBookUserRepository = broBookUserRepository;
        this.cohortRepository = cohortRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void seed() {
        if (cohortRepository.count() == 0) {
            cohortSeed();
        }
        if (broBookUserRepository.count() == 0) {
            userSeed();
            studentSeed();
            teacherSeed();
        }
    }

    private void userSeed() {
        BroBookUser beheerder = new BroBookUser(
                "De", "Beheerder",
                passwordEncoder.encode("beheerder"), "ADMIN");
        broBookUserRepository.save(beheerder);
    }

    public void studentSeed() {
        List<BroBookUser> students = readCsv("seedData/students.csv", BroBookUser.class);
        List<Cohort> allCohorts = cohortRepository.findAll();

        for (int i = 0; i < students.size(); i++) {
            BroBookUser student = students.get(i);
            student.getCohorts().add(allCohorts.get(i % allCohorts.size()));
            String testImageUrl = "https://i.pravatar.cc/300?img=" + ((i % 70) + 1);
            Image savedImage = imageService.saveImageFromUrl(testImageUrl);
            student.setProfilePicture(savedImage);
            student.setPassword(passwordEncoder.encode(defaultPassword));
            broBookUserRepository.save(student);
        }

        log.info("Teststudenten aangemaakt");
    }

    public void teacherSeed() {
        List<BroBookUser> teachers = readCsv("seedData/teachers.csv", BroBookUser.class);
        List<Cohort> allCohorts = cohortRepository.findAll();

        for (int i = 0; i < teachers.size(); i++) {
            BroBookUser teacher = teachers.get(i);
            if (i < 2) {
                teacher.getCohorts().addAll(allCohorts);
            } else {
                List<Cohort> shuffledCohorts = new ArrayList<>(allCohorts);
                Collections.shuffle(shuffledCohorts, random);
                int amount = 2 + random.nextInt(allCohorts.size() - 1);
                teacher.getCohorts().addAll(shuffledCohorts.subList(0, amount));
            }

            String testImageUrl = "https://i.pravatar.cc/300?img=" + ((i % 70) + 1);
            Image savedImage = imageService.saveImageFromUrl(testImageUrl);
            teacher.setProfilePicture(savedImage);
            teacher.setPassword(passwordEncoder.encode(defaultPassword));
            broBookUserRepository.save(teacher);
        }

        log.info("Testdocenten aangemaakt");
    }

    private void cohortSeed() {
        cohortRepository.save(new Cohort("Cohort 1", "Software Engineering"));
        cohortRepository.save(new Cohort("Cohort 2", "Functioneel Beheer"));
        cohortRepository.save(new Cohort("Cohort 3", "Software Engineering"));
        cohortRepository.save(new Cohort("Cohort 4", "Functioneel Beheer"));
        cohortRepository.save(new Cohort("Cohort 5", "Software Engineering"));
        cohortRepository.save(new Cohort("Cohort 6", "Software Engineering"));
        cohortRepository.save(new Cohort("Cohort 7", "Functioneel Beheer"));
        cohortRepository.save(new Cohort("Cohort 8", "Software Engineering"));
        cohortRepository.save(new Cohort("Cohort 9", "Functioneel Beheer"));
        cohortRepository.save(new Cohort("Cohort 10", "Software Engineering"));
        cohortRepository.save(new Cohort("Cohort 11", "Functioneel Beheer"));
        cohortRepository.save(new Cohort("Cohort 12", "Software Engineering"));
        cohortRepository.save(new Cohort("Cohort 13", "Software Engineering"));
    }

    private <T> List<T> readCsv(String resourcePath, Class<T> type) {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        try {
            Reader reader = new InputStreamReader(resource.getInputStream());
            return new CsvToBeanBuilder<T>(reader)
                    .withType(type)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
        } catch (IOException ioException) {
            throw new IllegalStateException("CSV file could not be loaded: " + resourcePath, ioException);
        }
    }
}
