package nl.miwnn.ch19.binarybros.brobook.controller;

/*
 * @author Mart Stukje
 * */

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class IndexController {

    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @GetMapping("/")
    public String showIndexPage(@AuthenticationPrincipal BroBookUser user) {

        if (user != null) {
            switch (user.getRole()) {
                case STUDENT -> {
                    log.debug("Student {} ingelogd, detailpagina wordt geladen", user.getFullName());
                    return "redirect:/info/detail/" + user.getId();
                }
                case TEACHER -> {
                    log.debug("Docent {} ingelogd, cohortoverzicht wordt geladen", user.getFullName());
                    return "redirect:/cohort/all";
                }
                case ADMIN -> {
                    log.debug("Admin {} ingelogd, gebruikersoverzicht wordt geladen", user.getUsername());
                    return "redirect:/user/all";
                }
                default -> {
                    throw new IllegalStateException("Onbekende rol: " + user.getRole());
                }
            }
        }
        log.debug("Indexpagina is geladen");
        return "index";
    }
}
