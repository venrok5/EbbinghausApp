package ru.alex.project.EbbinghausApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import ru.alex.project.EbbinghausApp.models.Person;
import ru.alex.project.EbbinghausApp.services.RegistrationService;
import ru.alex.project.EbbinghausApp.util.UserValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final UserValidator userValidator;

    @Autowired
    public AuthController(RegistrationService registrationService, UserValidator userValidator) {
        this.registrationService = registrationService;
        this.userValidator = userValidator;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
    	userValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "/auth/registration";

        registrationService.register(person);

        return "redirect:/auth/login";
    }
}