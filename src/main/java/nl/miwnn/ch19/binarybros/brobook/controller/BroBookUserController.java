package nl.miwnn.ch19.binarybros.brobook.controller;

/*
 * @author Mart Stukje
 * !! Doel van programma
 * */

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.service.BroBookUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BroBookUserController {

    private final BroBookUserService broBookUserService;

    private final List<BroBookUser> users = new ArrayList<>();

    public BroBookUserController(BroBookUserService broBookUserService) {
        this.broBookUserService = broBookUserService;
        users.add(new BroBookUser("Mart", "Stukje", "user"));
        users.add(new BroBookUser("Paul", "Rademaker", "user"));
    }

    @GetMapping("/user/all")
    public String showUserOverview(Model model){

        model.addAttribute("allUsers", users);
        return "/user/overview";
    }

    @GetMapping("/info/add")
    public String showNewInfoForm(Model model) {
        model.addAttribute("newUser", new BroBookUser());

        return "/user/info-form";
    }

    @PostMapping("/info/save")
    public String saveInfoForm(@ModelAttribute BroBookUser newUser) {
        newUser.setRole("user");
        users.add(newUser);
        return "redirect:/user/all";
    }

}
