package ru.alex.project.EbbinghausApp.repositories;

import org.springframework.stereotype.Repository;
import ru.alex.project.EbbinghausApp.models.Person;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
	Optional<Person> findByEmail(String email);
}
