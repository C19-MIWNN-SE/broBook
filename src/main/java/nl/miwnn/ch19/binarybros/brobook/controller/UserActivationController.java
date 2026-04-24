package nl.miwnn.ch19.binarybros.brobook.controller;

/*
 * @author Mart Stukje
 * Handles requests regarding the activation tokens for creating a broBook user
 * */

import jakarta.validation.Valid;
import nl.miwnn.ch19.binarybros.brobook.dto.PasswordFormDTO;
import nl.miwnn.ch19.binarybros.brobook.dto.TokenFormDTO;
import nl.miwnn.ch19.binarybros.brobook.service.UserActivationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserActivationController {

    private final UserActivationService userActivationService;
    private final static Logger log = LoggerFactory.getLogger(UserActivationController.class);

    public UserActivationController(UserActivationService userActivationService) {
        this.userActivationService = userActivationService;
    }

    @GetMapping("/activate/user")
    public String showActivationPage(Model model) {
        model.addAttribute("tokenFormDTO", new TokenFormDTO());
        return "activations/user-step1";
    }

    @PostMapping("/activate/user")
    public String verifyActivationToken(@Valid @ModelAttribute("tokenFormDTO") TokenFormDTO dto,
                                        BindingResult bindingResult) {
        String token = dto.getToken();
        log.info("Token ingevoerd voor activatie: {}", token);
        if (bindingResult.hasErrors()) {
            return "activations/user-step1";
        }
        if (!userActivationService.tokenIsActive(token)) {
            log.info("Niet herkende activatiecode: {}", token);
            bindingResult.rejectValue(
                    "token", "doesNotExist", "Deze activatiecode wordt niet herkend.");
            return "activations/user-step1";
        }
        return "redirect:/activate/user/" + token;
    }

    @GetMapping("/activate/user/{token}")
    public String showPasswordForm(@PathVariable String token, Model model) {
        model.addAttribute("passwordDTO", new PasswordFormDTO());
        model.addAttribute("token", token);
        return "activations/user-step2";
    }

    @PostMapping("/activate/user/{token}")
    public String savePassword(@Valid @ModelAttribute("passwordDTO") PasswordFormDTO dto,
                               BindingResult bindingResult,
                               @PathVariable String token) {
        log.info("Wachtwoord opslaan met token: {}", token);

        if (!dto.getPlainPassword().equals(dto.getCheckPassword())) {
            bindingResult.rejectValue(
                    "checkPassword", "confirmDoesNotMatch",
                    "De opgegeven wachtwoorden komen niet overeen");
        }

        if (bindingResult.hasErrors()) {
            log.info("Validatiefouten bij opslaan wachtwoord: {}", bindingResult.getErrorCount());
            return "activations/user-step2";
        }

        userActivationService.savePasswordWithToken(dto.getPlainPassword(), token);

        return "redirect:/login";
    }

    @PostMapping("/activate/print")
    public String printActivations(@RequestParam List<Long> selectedUsers, Model model) {
        log.debug("Printpagina opgevraagd voor ids: {}", selectedUsers);
        model.addAttribute("activations", userActivationService.findAllById(selectedUsers));
        return "activations/print";
    }
}
