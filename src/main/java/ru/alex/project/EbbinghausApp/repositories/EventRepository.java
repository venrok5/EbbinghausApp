package ru.alex.project.EbbinghausApp.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.alex.project.EbbinghausApp.models.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
	Optional<List<Event>> findByEventTimeBeforeAndIsComplete(Date date, boolean isComplete);
}
