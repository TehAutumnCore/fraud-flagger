package com.gary.fraudflagger.controller;

import com.gary.fraudflagger.repository.FraudFlagRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller //Tells spring this class handles web requests
public class DashboardController {

    private final FraudFlagRepository fraudFlagRepository;

    public DashboardController(FraudFlagRepository fraudFlagRepository) {
        this.fraudFlagRepository = fraudFlagRepository;
    }

    @GetMapping("/dashboard") //When someone visits/dashboard with a GET request, run this method"
    public String dashboard(Model model) { //a container you use to pass data from Java into HTML template
        model.addAttribute("flags", fraudFlagRepository.findAll()); //puts the list of fraud flags into the template under the name "flags"
        return "dashboard"; //tells Thymleaf to render the file templates/dashboard.html
    }

    @GetMapping("/")
    public String home(){
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
}
