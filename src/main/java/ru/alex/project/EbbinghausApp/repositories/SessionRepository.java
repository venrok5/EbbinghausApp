package ru.alex.project.EbbinghausApp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.alex.project.EbbinghausApp.models.Person;
import ru.alex.project.EbbinghausApp.models.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    Optional<Session> findByNameAndCreator(String name, Person person); 
    List<Session> findByCreator(Person person);
}
