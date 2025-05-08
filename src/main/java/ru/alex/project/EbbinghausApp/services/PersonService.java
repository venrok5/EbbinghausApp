package ru.alex.project.EbbinghausApp.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.alex.project.EbbinghausApp.models.Person;
import ru.alex.project.EbbinghausApp.models.Session;
import ru.alex.project.EbbinghausApp.repositories.PersonRepository;

@Service
@Transactional(readOnly = true)
public class PersonService {
	
	private final PersonRepository personRepository;
	
	@Autowired
    public PersonService(PersonRepository personRepository) { 
        this.personRepository = personRepository;
    }
	
	public List<Session> getSessionsForPersonWithId(int id) {
		Optional<Person> person = personRepository.findById(id);
		
		if (person.isPresent()) {
			Hibernate.initialize(person.get().getSessions());
			
			Collections.sort(person.get().getSessions());
			
			return person.get().getSessions(); // unwrap
		} else {
			return Collections.emptyList();
		}
	}
	
	@Transactional
	public void save(Person person) {
		personRepository.save(person);
	}
	
	@Transactional
	public void update(int id, Person updatedPerson) {
		updatedPerson.setPerson_id(id);
		personRepository.save(updatedPerson);
	}
	
	@Transactional
	public void delete(int person_id) {
		personRepository.deleteById(person_id);
	}
	
}
