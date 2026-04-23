package nl.miwnn.ch19.binarybros.brobook.controller;

import jakarta.validation.Valid;
import nl.miwnn.ch19.binarybros.brobook.dto.NewUserFormDTO;
import nl.miwnn.ch19.binarybros.brobook.dto.UserInfoFormDTO;
import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
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

    @GetMapping("/user/add")
    public String showNewUserForm(Model model) {
        log.debug("Formulier voor toevoegen nieuwe gebruiker opgevraagd");
        model.addAttribute("newUserDto", new NewUserFormDTO());
        model.addAttribute("allCohorts", cohortService.findAll());
        return "user/add-form";
    }

    @PostMapping("/user/save")
    public String saveNewUserForm(@ModelAttribute("newUserDto") @Valid NewUserFormDTO dto,
                                  BindingResult bindingResult,
                                  Model model) {
        log.info("Nieuwe gebruiker opslaan: {}", dto.getUsername());

        if (bindingResult.hasErrors()) {
            log.info("Validatiefouten bij opslaan nieuwe gebruiker: {}", bindingResult.getErrorCount());
            model.addAttribute("allCohorts", cohortService.findAll());
            return "user/add-form";
        }

        broBookUserService.saveNewUser(dto);
        return "redirect:/user/all";
    }

    @GetMapping("/info/edit/{id}")
    public String showNewInfoForm(@PathVariable Long id,
                                  Model model) {
        log.debug("Formulier voor toevoegen gebruikersinformatie opgevraagd voor gebruiker met id: {}", id);

        UserInfoFormDTO dto = broBookUserService.getUserInfoFormDTO(id);
        model.addAttribute("dtoToEdit", dto);
        return "user/info-form";
    }

    @PostMapping("/info/save")
    public String saveInfoForm(@ModelAttribute("dtoToEdit") @Valid UserInfoFormDTO dto,
                               BindingResult bindingResult,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               Model model) {
        log.info("Gebruiker opslaan: {}", dto.getId());
        if (bindingResult.hasErrors()) {
            log.warn("Validatiefouten bij opslaan gebruikersinformatie: {}", bindingResult.getErrorCount());
            return "user/info-form";
        }
        broBookUserService.saveUserInformation(dto, imageFile);

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

    @PostMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        broBookUserService.deleteById(id);
        return "redirect:/user/all";
    }


}