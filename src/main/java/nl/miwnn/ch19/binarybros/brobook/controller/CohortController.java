package nl.miwnn.ch19.binarybros.brobook.controller;

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.Cohort;
import nl.miwnn.ch19.binarybros.brobook.service.CohortService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Paul Rademaker
 * Handles all requests regarding cohorts
 */

@Controller
public class CohortController {

    private static final int VISIBLE_USER_BUBBLES = 4;
    private final CohortService cohortService;

    public CohortController(CohortService cohortService) {
        this.cohortService = cohortService;
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
    public String showCohortOverview(Model model) {
        List<Cohort> allCohorts = cohortService.findAll();

        Map<Long, List<BroBookUser>> visibleUsersMap = new HashMap<>();
        Map<Long, Integer> overflowMap = new HashMap<>();

        for (Cohort cohort : allCohorts) {
            List<BroBookUser> visibleUsers = cohort
                    .getParticipants().stream()
                    .limit(VISIBLE_USER_BUBBLES).toList();
            int overflow = cohort.getParticipants().size() - VISIBLE_USER_BUBBLES;

            visibleUsersMap.put(cohort.getId(), visibleUsers);
            overflowMap.put(cohort.getId(), overflow);
        }

        model.addAttribute("allCohorts", allCohorts);
        model.addAttribute("visibleUsersMap", visibleUsersMap);
        model.addAttribute("overflowMap", overflowMap);
        return "cohort/overview";
    }

    @GetMapping("/cohort/details/{id}")
    public String showCohortDetails(@PathVariable("id") Long id, Model model){
        Cohort cohort = cohortService.getCohortById(id);

        model.addAttribute("selectedCohort", cohort);
        return "cohort/details";
    }
}
