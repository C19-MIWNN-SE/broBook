package nl.miwnn.ch19.binarybros.brobook.controller;

/*
 * @author Mart Stukje
 * !! Doel van programma
 * */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class IndexController {

    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @GetMapping("/")
    public String showIndexPage(Model model) {

        log.debug("Pagina is geladen");
        model.addAttribute("productName", "broBook");
        model.addAttribute("currentTime", LocalDateTime.now());
        return "index";
    }
}
