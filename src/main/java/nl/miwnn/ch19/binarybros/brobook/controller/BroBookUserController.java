package nl.miwnn.ch19.binarybros.brobook.controller;

import jakarta.validation.Valid;
import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.Cohort;
import nl.miwnn.ch19.binarybros.brobook.service.BroBookUserService;
import nl.miwnn.ch19.binarybros.brobook.service.CohortService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Controller
public class BroBookUserController {

    private static final Logger log = LoggerFactory.getLogger(BroBookUserController.class);
    private final BroBookUserService broBookUserService;
    private final CohortService cohortService;

    public BroBookUserController(BroBookUserService broBookUserService, CohortService cohortService) {
        this.broBookUserService = broBookUserService;
        this.cohortService = cohortService;
    }

    @GetMapping("/user/all")
    public String showUserOverview(Model model, Principal principal) {
        BroBookUser currentUser = broBookUserService.getUserByUsername(principal.getName());

        model.addAttribute("allUsers", broBookUserService.findVisibleUsers(currentUser));

        return "user/overview";
    }

    @GetMapping("/info/add")
    public String showNewInfoForm(Model model) {
        log.debug("Formulier voor toevoegen gebruikersinformatie opgevraagd");
        model.addAttribute("newUser", new BroBookUser());

        List<Cohort> cohorts = cohortService.findAll();

        model.addAttribute("allCohorts", cohorts);

        return "user/info-form";
    }

    @PostMapping("/info/save")
    public String saveInfoForm(@ModelAttribute("newUser") @Valid BroBookUser newUser,
                               BindingResult bindingResult,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               Model model) {
        log.info("Gebruiker opslaan: {}", newUser.getUsername());
        newUser.setRole("user");
        if (bindingResult.hasErrors()) {
            log.warn("Validatiefouten bij opslaan gebruikersinformatie: {}", bindingResult.getErrorCount());
            model.addAttribute("allCohorts", cohortService.findAll());
            return "user/info-form";
        }
        broBookUserService.save(newUser, imageFile);

        return "redirect:/user/all";
    }

    @GetMapping("/info/detail/{id}")
    public String getDetailPage(@PathVariable Long id, Model model, Principal principal) {
        BroBookUser currentUser = broBookUserService.getUserByUsername(principal.getName());
        BroBookUser targetUser = broBookUserService.getUserById(id);

        List<BroBookUser> allowedUsers = broBookUserService.findVisibleUsers(currentUser);

        if (targetUser == null || !allowedUsers.contains(targetUser)) {
            return "redirect:/user/all?unauthorized";
        }

        model.addAttribute("shownUser", targetUser);
        model.addAttribute("userAge",
                targetUser.getBirthDate() != null ?
                        Period.between(targetUser.getBirthDate(), LocalDate.now()).getYears()
                        : null);
        return "user/details";
    }
}