package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import project.persistence.entities.User;
import org.springframework.ui.Model;
import project.service.CustomUserDetailsService;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller for the "/" and some get requests that are less related to users and changing and saving bets.
 */


@Controller
public class HomeController {

    CustomUserDetailsService customUserDetailsService;

    // Dependency Injection
    @Autowired
    public HomeController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }



}
