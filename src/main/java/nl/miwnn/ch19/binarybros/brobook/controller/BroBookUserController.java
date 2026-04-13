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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class BroBookUserController {

    private final BroBookUserService broBookUserService;

    public BroBookUserController(BroBookUserService broBookUserService) {
        this.broBookUserService = broBookUserService;
    }

    @GetMapping("/all")
    public String showUserOverview(Model model){
        List<BroBookUser> users = new ArrayList<>();
        users = broBookUserService.getAllUsers();

        model.addAttribute("allUsers", users);
        return "/user/overview";
    }

}
