package ru.alex.project.EbbinghausApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.alex.project.EbbinghausApp.models.Person;
import ru.alex.project.EbbinghausApp.repositories.PersonRepository;

@Service
public class RegistrationService {
	private final PersonRepository personRepository;
	
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword())); // encode password before save to DB
        personRepository.save(person);
    }
}
