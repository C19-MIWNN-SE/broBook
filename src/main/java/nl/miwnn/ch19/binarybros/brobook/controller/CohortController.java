package nl.miwnn.ch19.binarybros.brobook.controller;

import nl.miwnn.ch19.binarybros.brobook.model.Cohort;
import nl.miwnn.ch19.binarybros.brobook.service.CohortService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Paul Rademaker
 * ---- Programma dat dingen doet ----
 * ---- VERVANG MIJ ----
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

}
