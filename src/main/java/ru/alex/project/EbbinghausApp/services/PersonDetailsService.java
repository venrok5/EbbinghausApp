package ru.alex.project.EbbinghausApp.services;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ru.alex.project.EbbinghausApp.models.Person;
import ru.alex.project.EbbinghausApp.repositories.PersonRepository;
import ru.alex.project.EbbinghausApp.security.PersonDetails;

@Service
public class PersonDetailsService implements UserDetailsService {

	private static final Logger logger = LogManager.getLogger(PersonDetailsService.class);
	
    private final PersonRepository personRepository;

    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByEmail(s);

        if (person.isEmpty()) {
        	String msg = "User not found";
			logger.info(msg);
            throw new UsernameNotFoundException(msg);
        }

        return new PersonDetails(person.get());
    }
}
