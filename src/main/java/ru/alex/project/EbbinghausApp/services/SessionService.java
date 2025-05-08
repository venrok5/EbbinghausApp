package ru.alex.project.EbbinghausApp.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.alex.project.EbbinghausApp.models.Event;
import ru.alex.project.EbbinghausApp.models.Person;
import ru.alex.project.EbbinghausApp.models.Session;
import ru.alex.project.EbbinghausApp.repositories.EventRepository;
import ru.alex.project.EbbinghausApp.repositories.SessionRepository;
import ru.alex.project.EbbinghausApp.util.ExtendedDate;

@Service
@Transactional(readOnly = true)
public class SessionService {
	
	private final SessionRepository sessionRepository;
	private final EventRepository eventRepository;
	
	@Autowired
	public SessionService(SessionRepository sessionRepository, EventRepository eventRepository) {
		this.sessionRepository = sessionRepository;	
		this.eventRepository = eventRepository;
	}
	
	public List<Session>getSessionsForPerson(Person person) {
		return sessionRepository.findByCreator(person);
	}
	
	public Session getSessionsById(int id) {
		Optional<Session> session = sessionRepository.findById(id);
		
		return session.orElse(null);
	}
	
	public Session getSessionByName(String name, Person person) {
		return sessionRepository.findByNameAndCreator(name, person).orElse(null);
	}
	
	public List<Event> getEventsForSessionWithId(int id) { 
		Optional<Session> session = sessionRepository.findById(id);
		
		if (session.isPresent()) {
			Hibernate.initialize(session.get().getEvents());
			
			Collections.sort(session.get().getEvents());
			
			List<Event> events = new ArrayList<>();
					
			for (Event event : session.get().getEvents()) { // unwrap
				event.setPositionNumber(ExtendedDate.getNumberDaysBetween(session.get().getStartDate(), event.getEventTime()));
				events.add(event);
			}
			
			for (int i=0; i<events.size(); i++) {
				events.get(i).setIndex(i);
			}
			
			return events;
		} else {
			return Collections.emptyList();
		}
	}
	
	public int getCreatorsIdForSessionWithId(int id) {
		return getSessionsById(id).getCreator().getPerson_id();
	}
	
	public Optional<Event> getCurrentEvent(int sessionId) {
		List<Event> events = new ArrayList<>();
		events = getEventsForSessionWithId(sessionId);
		
		for (int i=0; i<events.size(); i++) {
			if (!events.get(i).getIsComplete()) {
				return Optional.of(events.get(i));
			}
		}
		return null;
	}
	
	@Transactional
	public int save(Session session, Person user) {
		session.setCreator(user);
		int id = sessionRepository.save(session).getSession_id();
		
		List<Event> events = Event.makeEvents(session.getStartDate(), session.getEndDate());
		for (Event event : events) {
			event.setSession(session);
			eventRepository.save(event);
		}
		return id;
	}
	
	@Transactional
	public void update(int id, Session updatedSession) {
		updatedSession.setSession_id(id);
		sessionRepository.save(updatedSession);
	}
	
	@Transactional
	public void delete(int session_id) {
		sessionRepository.deleteById(session_id);
	}
	
	public String buildPathStr(int id) {
		int userId = getCreatorsIdForSessionWithId(id);
		return "/"+Integer.toString(userId)+"-"+Integer.toString(id);
	}

}
