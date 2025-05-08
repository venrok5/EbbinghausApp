package ru.alex.project.EbbinghausApp.services;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.alex.project.EbbinghausApp.models.Event;
import ru.alex.project.EbbinghausApp.models.Session;
import ru.alex.project.EbbinghausApp.repositories.EventRepository;

@Service
public class EventService {

	private final EventRepository eventRepository;
	
	@Autowired
	public EventService(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}
	
	
	public List<Event> getEventsBeforeDate(Date date) {
		Optional<List<Event>> events = eventRepository.findByEventTimeBeforeAndIsComplete(date, false);
		
		if (events.isPresent()) {
			return events.get();
		} else {
			return Collections.emptyList();
		}
	}
	
	public String getCreatorEmail(Event event) {
		Session session = event.getSession();
		
		return session.getCreator().getEmail();
	}
	
	public String getSessionTitle(Event event) {
		Session session = event.getSession();
		
		return session.getName();
	}
	
	public String getCreatorsName(Event event) {
		Session session = event.getSession();
		
		return session.getCreator().getUsername();
	}
	
	@Transactional
	public void setComplete(int id) {
		Optional<Event> event = eventRepository.findById(id);
		if (event.isPresent()) {
			Event updatedEvent = event.get();
			updatedEvent.setIsComplete(true);
			update(id, updatedEvent);
		}
	}
	
	@Transactional
	public void update(int id, Event updatedEvent) {
		updatedEvent.setEvent_id(id);
		eventRepository.save(updatedEvent);
	}
}
