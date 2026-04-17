package nl.miwnn.ch19.binarybros.brobook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Paul Rademaker
 * Handle request for logging in
 */

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage(){
        return "login";

    }
}
