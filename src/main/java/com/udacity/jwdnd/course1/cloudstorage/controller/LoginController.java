package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        logger.info("GET /login was hit");
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        logger.info("GET /signup was hit");
        return "signup";
    }

    @PostMapping("/signup")
    public String signupHandler(@ModelAttribute User user, Model model) {
        logger.info("POST /signup was hit with user data: {}", user.toString());
        String signupError = null;
        if (userService.getUserByUsername(user.getUsername()) != null) {
            signupError = "The username already exists. Username: " + user.getUsername();
            model.addAttribute("signupError", signupError);
        return "signup";
        }

            Integer result = userService.insertUser(user);
            if (result > 0) {
                model.addAttribute("signupSuccess", true);
                return "signup";
            } else {
                model.addAttribute("error", true);
                model.addAttribute("errorMsg", "There was an error signing you up. Please, try again later.");
            }
        return "result";
    }

    @GetMapping("/logout")
    public String handleLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout";
    }

    @PostConstruct
    public void insertTestUser(){
        String username = "test";
        String password = "test";
        String firstname = "admin";
        String lastname = "test";

        User testUser = new User(username, "", password, firstname, lastname);
        userService.insertUser(testUser);
        logger.info("Test user was successfully added");
    }
}
