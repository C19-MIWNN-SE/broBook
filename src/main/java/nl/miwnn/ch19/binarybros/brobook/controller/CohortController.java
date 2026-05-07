package nl.miwnn.ch19.binarybros.brobook.controller;

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.Cohort;
import nl.miwnn.ch19.binarybros.brobook.model.Role;
import nl.miwnn.ch19.binarybros.brobook.repository.BroBookUserRepository;
import nl.miwnn.ch19.binarybros.brobook.service.BroBookUserService;
import nl.miwnn.ch19.binarybros.brobook.service.CohortService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Paul Rademaker
 * Handles all requests regarding cohorts
 */

@Controller
public class CohortController {

    private static final int VISIBLE_USER_BUBBLES = 3;

    private final CohortService cohortService;
    private final BroBookUserService broBookUserService;
    private final BroBookUserRepository broBookUserRepository;

    public CohortController(CohortService cohortService,
                            BroBookUserService broBookUserService,
                            BroBookUserRepository broBookUserRepository) {
        this.cohortService = cohortService;
        this.broBookUserService = broBookUserService;
        this.broBookUserRepository = broBookUserRepository;
    }

    @GetMapping("/cohort/add")
    public String showNewCohortForm(Model model) {
        model.addAttribute("newCohort", new Cohort());
        return "cohort/form";
    }

    @PostMapping("/cohort/save")
    public String saveCohort(@ModelAttribute Cohort cohort){

        cohortService.save(cohort);
        return "redirect:/cohort/all";
    }

    @GetMapping("/cohort/all")
    public String showCohortOverview(Model model, Principal principal) {
        String username = principal.getName();
        Optional<BroBookUser> currentUser = broBookUserRepository.findByUsername(username);

        List<Cohort> displayCohorts;
        String overviewTitle;

        if (currentUser.isPresent()) {
            BroBookUser user = currentUser.get();

            if (user.getRole() == Role.STUDENT || user.getRole() == Role.TEACHER) {
                displayCohorts = user.getCohorts();
                overviewTitle = "Mijn Cohorten";
            } else {
                displayCohorts = cohortService.findAll();
                overviewTitle = "Alle Cohorten";
            }
        } else {
            displayCohorts = cohortService.findAll();
            overviewTitle = "Alle Cohorten";
        }

        Map<Long, List<BroBookUser>> visibleUsersMap = new HashMap<>();
        Map<Long, Integer> overflowMap = new HashMap<>();

        for (Cohort cohort : displayCohorts) {
            List<BroBookUser> visibleUsers = cohort.getParticipants().stream()
                    .limit(VISIBLE_USER_BUBBLES)
                    .toList();

            int overflow = Math.max(0, cohort.getParticipants().size() - VISIBLE_USER_BUBBLES);

            visibleUsersMap.put(cohort.getId(), visibleUsers);
            overflowMap.put(cohort.getId(), overflow);
        }

        model.addAttribute("allCohorts", displayCohorts);
        model.addAttribute("overviewTitle", overviewTitle);
        model.addAttribute("visibleUsersMap", visibleUsersMap);
        model.addAttribute("overflowMap", overflowMap);

        return "cohort/overview";
    }

    @GetMapping("/cohort/details/{id}")
    public String showCohortDetails(@PathVariable Long id, Model model){
        Cohort cohort = cohortService.getCohortById(id);

        model.addAttribute("selectedCohort", cohort);
        model.addAttribute("allUsers", broBookUserService.findAll());
        model.addAttribute("students", broBookUserService.getParticipantsByRole(cohort, Role.STUDENT));
        model.addAttribute("teachers", broBookUserService.getParticipantsByRole(cohort, Role.TEACHER));
        return "cohort/details";
    }

    @PostMapping("/cohort/{id}/add-manual")
    public String addUserManual(@PathVariable Long id, @RequestParam Long userId) {
        Cohort cohort = cohortService.getCohortById(id);
        BroBookUser user = broBookUserRepository.findById(userId).orElse(null);

        if (cohort != null && user != null) {
            user.getCohorts().add(cohort);
            broBookUserRepository.save(user);
        }
        return "redirect:/cohort/details/" + id;
    }

    @PostMapping("/cohort/{id}/import")
    public String importToSpecificCohort(@PathVariable Long id, @RequestParam("csvFile") MultipartFile file) {
        try {
            broBookUserService.importUsersFromCsv(file, id);
        } catch (Exception e) {
            // Log error
        }
        return "redirect:/cohort/details/" + id;
    }
}
