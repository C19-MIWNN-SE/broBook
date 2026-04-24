package nl.miwnn.ch19.binarybros.brobook.controller;

/*
 * @author Mart Stukje
 * Handles requests regarding the activation tokens for creating a broBook user
 * */

import nl.miwnn.ch19.binarybros.brobook.service.UserActivationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserActivationController {

    private final UserActivationService userActivationService;
    private final static Logger log = LoggerFactory.getLogger(UserActivationController.class);

    public UserActivationController(UserActivationService userActivationService) {
        this.userActivationService = userActivationService;
    }

    @PostMapping("/activation/print")
    public String printActivations(@RequestParam List<Long> selectedUsers, Model model) {
        log.debug("Printpagina opgevraagd voor ids: {}", selectedUsers);
        model.addAttribute("activations", userActivationService.findAllById(selectedUsers));
        return "activations/print";
    }
}
