package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.config.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/credentials")
public class CredentialController {
    private final CredentialService credService;
    private final AuthenticationService authService;
    public CredentialController(CredentialService credService, AuthenticationService authService) {
        this.credService = credService;
        this.authService = authService;
    }

    @GetMapping
    public String getAllCreds(Model model, Principal principal){
        if(principal == null || authService.getCurrentUser() == null){
            return "redirect:/login";
        }
        model.addAttribute("credentialList", credService.findAllCredentials());
        model.addAttribute("page", "credentials");
        return "home";
    }

    @PostMapping
    public String createCred(@ModelAttribute Credential credential, Model model){
        if(authService.getCurrentUser() == null){
            return "redirect:/login";
        }
        model.addAttribute("page", "credentials");
        if(credential != null && !credential.getPassword().isBlank() && !credential.getUrl().isBlank() && !credential.getUsername().isBlank()){
            int rowsUpdated = credService.insertCredential(credential);
            if(rowsUpdated == 0 ){
                model.addAttribute("error", true);
            }else {
                model.addAttribute("success", true);
            }
        }
        return "redirect:credentials";
    }
}
