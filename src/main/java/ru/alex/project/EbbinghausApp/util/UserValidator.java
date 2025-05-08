package ru.alex.project.EbbinghausApp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ru.alex.project.EbbinghausApp.models.Person;
import ru.alex.project.EbbinghausApp.services.PersonDetailsService;

@Component
public class UserValidator implements Validator {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public UserValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        try {
            personDetailsService.loadUserByUsername(person.getEmail());
        } catch (UsernameNotFoundException ignored) {
            return;
        }
        errors.rejectValue("email", "", "Accounbt with this email already exists");
    }

}
