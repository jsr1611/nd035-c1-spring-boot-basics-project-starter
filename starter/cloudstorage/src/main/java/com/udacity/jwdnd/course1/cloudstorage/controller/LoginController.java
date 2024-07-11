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
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);
    private UserService userService;

    public LoginController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(){
        logger.info("GET /login was hit");
        return "login";
    }

    @GetMapping("/signup")
    public String signup(){
        logger.info("GET /signup was hit");
        return "signup";
    }

    @PostMapping("/signup")
    public String signupHandler(@ModelAttribute User user, Model model){
        logger.info("POST /signup was hit with user data: " + user.toString());
        String signupError = null;
        if(userService.getUserByUsername(user.getUsername()) != null){
            signupError = "The username already exists. Username: [" + user.getUsername() + "]";
        }

        if(signupError == null){
            Integer result = userService.insertUser(user);
            if(result > 0){
                model.addAttribute("signupSuccess", true);
            }else {
                model.addAttribute("signupError", "There was an error signing you up. Please try again.");
            }
        }
        return "signup";
    }

    @GetMapping("/logout")
    public String handleLogout(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout";
    }
}
