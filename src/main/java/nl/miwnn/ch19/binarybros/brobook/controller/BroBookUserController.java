package nl.miwnn.ch19.binarybros.brobook.controller;

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.service.BroBookUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class BroBookUserController {

    private final BroBookUserService broBookUserService;


    public BroBookUserController(BroBookUserService broBookUserService) {
        this.broBookUserService = broBookUserService;

    }

    @GetMapping("/user/all")
    public String showUserOverview(Model model){
        model.addAttribute("allUsers", broBookUserService.findAll());
        return "user/overview";
    }

    @GetMapping("/info/add")
    public String showNewInfoForm(Model model) {
        model.addAttribute("newUser", new BroBookUser());
        return "user/info-form";
    }

    @PostMapping("/info/save")
    public String saveInfoForm(@ModelAttribute BroBookUser newUser) {
        newUser.setRole("user");
        broBookUserService.save(newUser);

        return "redirect:/user/all";
    }
}