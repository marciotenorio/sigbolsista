package br.com.sigteam.sigbolsista.controllers;

import br.com.sigteam.sigbolsista.exceptions.UserAlreadyExistException;
import br.com.sigteam.sigbolsista.models.User;
import br.com.sigteam.sigbolsista.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public ModelAndView postRegistration(
        User user, BindingResult bindingResult, Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("registrationForm", user);
            return new ModelAndView("register");
        }
        try {
            userService.register(user);
        } catch (UserAlreadyExistException e) {
            bindingResult.rejectValue("email", "user.username","Já existe um usuário com este username!");
            model.addAttribute("registrationForm", user);
            return new ModelAndView("register");
        }
        return new ModelAndView("redirect:/login");
    }
}
