package nl.miwnn.ch19.binarybros.brobook.controller;

import nl.miwnn.ch19.binarybros.brobook.model.Cohort;
import nl.miwnn.ch19.binarybros.brobook.service.CohortService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Paul Rademaker
 * Handles all requests regarding cohorts
 */

@Controller
public class CohortController {

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
        model.addAttribute("allCohorts", cohortService.findAll());
        return "cohort/overview";
    }

    @GetMapping("/cohort/details/{id}")
    public String showCohortDetails(@PathVariable("id") Long id, Model model){
        Cohort cohort = cohortService.getCohortById(id);

        model.addAttribute("selectedCohort", cohort);

        return "cohort/details";
    }
}
