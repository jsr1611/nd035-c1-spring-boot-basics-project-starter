package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.config.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/credentials")
public class CredentialController {
    private final CredentialService credService;
    private final AuthenticationService authService;
    private final EncryptionService encryptionService;
    private final Logger logger = LoggerFactory.getLogger(CredentialController.class);
    public CredentialController(CredentialService credService, AuthenticationService authService, EncryptionService encryptionService) {
        this.credService = credService;
        this.authService = authService;
        this.encryptionService = encryptionService;
    }

    @ModelAttribute
    public void addPage(Model model){
        model.addAttribute("page", "credentials");
    }

    @GetMapping
    public String getAllCredentials(Model model, Principal principal){
        if(principal == null || authService.getCurrentUser() == null){
            return "redirect:/login";
        }
        model.addAttribute("credentialList", credService.findAllCredentials());
        return "home";
    }

    @PostMapping
    public String createCredential(@ModelAttribute Credential credential, Model model){
        if(authService.getCurrentUser() == null){
            return "redirect:/login";
        }
        if(credential != null && !credential.getPassword().isBlank() && !credential.getUrl().isBlank() && !credential.getUsername().isBlank()){
            int rowsUpdated = 0;
            if(credential.getCredentialId() != null){
                rowsUpdated = credService.updateCredential(credential);
            }else {
                rowsUpdated = credService.insertCredential(credential);
            }
            if(rowsUpdated == 0 ){
                model.addAttribute("error", true);
            }else {
                model.addAttribute("success", true);
            }
        }
        return "redirect:credentials";
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteCredential(@RequestBody Map<String, Integer> requestBody){
        if(authService.getCurrentUser() == null){
            Map<String, String> data = new HashMap<>();
            data.put("data", "Unauthorized access. Please, login and try again later.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(data);
        }

        Integer credentialId = requestBody.get("credentialId");
        logger.info("POST request to delete a credential with id: {}", credentialId);
        Integer rowsUpdated = credService.deleteById(credentialId);
        if (rowsUpdated != null && rowsUpdated > 0) {
            return ResponseEntity.ok().body(Collections.singletonMap("success", true));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Note deletion was unsuccessful"));
        }
    }

    @PostMapping("/decrypt")
    public ResponseEntity<?> decryptCredentials(@RequestBody Map<String, String> requestBody){
        if(authService.getCurrentUser() == null){
            Map<String, String> data = new HashMap<>();
            data.put("data", "Unauthorized access. Please, login and try again later.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(data);
        }

        String ckey = requestBody.get("ckey");
        String encryptedPassword = requestBody.get("password");

        if("".equals(ckey) || "".equals(encryptedPassword)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Empty ckey or password. Please, contact admin. "));
        }
        String decryptedPassword = this.encryptionService.decryptValue(encryptedPassword, ckey);
        return ResponseEntity.ok().body(Collections.singletonMap("password", decryptedPassword));
    }
}
