package nl.miwnn.ch19.binarybros.brobook.controller;

import jakarta.validation.Valid;
import nl.miwnn.ch19.binarybros.brobook.dto.UserAccountFormDTO;
import nl.miwnn.ch19.binarybros.brobook.dto.UserInfoFormDTO;
import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.UserActivation;
import nl.miwnn.ch19.binarybros.brobook.service.BroBookUserService;
import nl.miwnn.ch19.binarybros.brobook.service.CohortService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("allCohorts", cohortService.findAll());
        return "user/overview";
    }

    @GetMapping("/user/add")
    public String showNewUserForm(Model model) {
        log.debug("Formulier voor toevoegen nieuwe gebruiker opgevraagd");
        model.addAttribute("userAccountDTO", new UserAccountFormDTO());
        model.addAttribute("allCohorts", cohortService.findAll());
        return "user/account-form";
    }

    @GetMapping("/user/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        log.debug("Formulier voor bewerken gebruiker opgevraagd met id: {}", id);
        model.addAttribute("userAccountDTO", broBookUserService.getUserAccountFormDTO(id));
        model.addAttribute("allCohorts", cohortService.findAll());
        return "user/account-form";
    }

    @PostMapping("/user/save")
    public String saveNewUserForm(@ModelAttribute("userAccountDTO") @Valid UserAccountFormDTO dto,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        log.info("Gebruiker opslaan: {}", dto.getUsername());

        if (broBookUserService.usernameAlreadyInUse(dto.getUsername(), dto.getId())) {
            bindingResult.rejectValue(
                    "username", "alreadyExists", "Gebruikersnaam bestaat al");
        }

        if (bindingResult.hasErrors()) {
            log.info("Validatiefouten bij opslaan nieuwe gebruiker: {}", bindingResult.getErrorCount());
            model.addAttribute("allCohorts", cohortService.findAll());
            return "user/account-form";
        }

        UserActivation activation = broBookUserService.saveUserAccount(dto);
        redirectAttributes.addFlashAttribute("newActivation", activation);
        return "redirect:/user/all";
    }

    @PostMapping("/user/import")
    public String importUsers(@RequestParam("csvFile") MultipartFile file,
                              @RequestParam("cohortId") Long cohortId,
                              RedirectAttributes ra) {
        try {
            broBookUserService.importUsersFromCsv(file, cohortId);
            ra.addFlashAttribute("success", "Gebruikers succesvol geïmporteerd en gekoppeld!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Fout bij import: " + e.getMessage());
        }
        return "redirect:/user/all";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        broBookUserService.deleteById(id);
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
        log.info("Gebruikersinformatie opslaan: {}", dto.getId());
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
}