package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.persistence.entities.User;
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

    // Method that returns the correct view for the URL /signup
    // This handles the GET request for this URL
    // Notice the `method = RequestMethod.GET` part
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView signupViewGet(){
        ModelAndView model = new ModelAndView();

        model.addObject("user",new User());
        model.setViewName("Signup");
        // Return the view
        return model;
    }

    // Method that receives the POST request on the URL /signup
    // and receives the ModelAttribute("user")
    // That attribute is the attribute that is mapped to the form, so here
    // we can save the user because we get the data that was entered
    // into the form.
    // Notice the `method = RequestMethod.POST` part
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signupViewPost(@ModelAttribute("user") User user, Model model){
        try {
            customUserDetailsService.save(user);
        } catch (Exception e){

            model.addAttribute("errorMsg", "Username taken");
            return "Signup";
        }
        model.addAttribute("errorMsg", "Login with your new account");
        // Return the view
        return "Login";
    }

    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String loginViewGet(){
        return "Login";
    }


    @RequestMapping(value="/userpage", method = RequestMethod.GET)
    public String userPageViewGet(Authentication authentication, Model model){
        User user = customUserDetailsService.findByUsername(authentication.getName());
        model.addAttribute("pendingBets", user.getPendingBets());

        model.addAttribute("waitingPendingBets", user.getWaitingPendingBets());
        model.addAttribute("notWaitingPendingBets", user.getNotWaitingPendingBets());

        model.addAttribute("resolvedBets", user.getResolvedBets());
        model.addAttribute("activeBets", user.getActiveBets());
        model.addAttribute("user", user);
        return "UserPage";
    }
}
