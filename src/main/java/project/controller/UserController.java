package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import project.service.CustomUserDetailsService;



/**
 * Controller for most post and get request that have something to do with the user
 * note that WebSecurityConfig handles the login post because of authentication
 */

@Controller
public class UserController {

    private CustomUserDetailsService customUserDetailsService;
    // Dependency Injection

    @Autowired
    public UserController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

}
