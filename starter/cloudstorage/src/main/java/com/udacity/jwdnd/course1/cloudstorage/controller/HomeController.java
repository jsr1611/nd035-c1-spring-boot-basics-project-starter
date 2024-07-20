package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.config.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final AuthenticationService authService;

    public HomeController(AuthenticationService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String gotohome(Model model, Principal principal){
        if(principal == null || authService.getCurrentUser() == null){
            return "redirect:/login";
        }
        return "home";
    }
}
