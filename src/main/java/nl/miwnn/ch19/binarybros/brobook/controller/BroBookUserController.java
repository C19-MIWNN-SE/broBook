package nl.miwnn.ch19.binarybros.brobook.controller;

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.Cohort;
import nl.miwnn.ch19.binarybros.brobook.service.BroBookUserService;
import nl.miwnn.ch19.binarybros.brobook.service.CohortService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Controller
public class BroBookUserController {

    private final BroBookUserService broBookUserService;
    private final CohortService cohortService;

    public BroBookUserController(BroBookUserService broBookUserService, CohortService cohortService) {
        this.broBookUserService = broBookUserService;
        this.cohortService = cohortService;
    }

    @GetMapping("/user/all")
    public String showUserOverview(Model model){
        model.addAttribute("allUsers", broBookUserService.findAll());
        return "user/overview";
    }

    @GetMapping("/info/add")
    public String showNewInfoForm(Model model) {

        model.addAttribute("newUser", new BroBookUser());

        List<Cohort> cohorts = cohortService.findAll();

        model.addAttribute("allCohorts", cohorts);

        return "user/info-form";
    }

    @PostMapping("/info/save")
    public String saveInfoForm(@ModelAttribute BroBookUser newUser,
                               @RequestParam("imageFile") MultipartFile imageFile) {

        newUser.setRole("user");
        broBookUserService.save(newUser, imageFile);

        return "redirect:/user/all";
    }

    @GetMapping("/info/detail/{id}")
    public String getDetailPage(@PathVariable Long id,
                                Model model) {
        BroBookUser broBookUser = broBookUserService.getUserById(id);

        model.addAttribute("shownUser", broBookUser);
        model.addAttribute("userAge",
                broBookUser.getBirthDate() != null ?
                        Period.between(broBookUser.getBirthDate(), LocalDate.now()).getYears()
                        : null);
        return "user/details";
    }
}