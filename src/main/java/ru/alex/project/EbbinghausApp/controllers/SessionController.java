package ru.alex.project.EbbinghausApp.controllers;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import ru.alex.project.EbbinghausApp.models.Event;
import ru.alex.project.EbbinghausApp.models.Person;
import ru.alex.project.EbbinghausApp.models.Session;
import ru.alex.project.EbbinghausApp.security.PersonDetails;
import ru.alex.project.EbbinghausApp.services.EventService;
import ru.alex.project.EbbinghausApp.services.FilesStorageService;
import ru.alex.project.EbbinghausApp.services.SessionService;
import ru.alex.project.EbbinghausApp.util.ExtendedDate;


@Controller
@RequestMapping("/session")
public class SessionController {
	private final SessionService sessionService;
	private final FilesStorageService storageService;
	private final EventService eventService;
	
	@Autowired
    public SessionController(SessionService sessionService, FilesStorageService storageService, EventService eventService) {
        this.sessionService = sessionService;
        this.storageService = storageService;
        this.eventService = eventService;
    }

    @GetMapping("/{id}")
    public String showSessionInfo(@PathVariable("id") int id, Model model) {
    	Session session = sessionService.getSessionsById(id);
    	Optional<Event> currentEvent = sessionService.getCurrentEvent(id);
    	String eventMessage = "";
    	
    	model.addAttribute("current", session);
    	model.addAttribute("events", sessionService.getEventsForSessionWithId(id));
    	model.addAttribute("daysCount", ExtendedDate.getNumberDaysBetween(session.getStartDate(), session.getEndDate()));
    	model.addAttribute("endTime", ExtendedDate.getNumberOfMinutesBetween(session.getStartDate(), session.getEndDate()));
    	
    	if (currentEvent.isPresent()) {
    		if (ExtendedDate.isPresent(currentEvent.get().getEventTime())) {
    			model.addAttribute("currentEvent", currentEvent.get());
    			eventMessage = "Current repetition " + currentEvent.get().getEventTime();
    		} else {
    			model.addAttribute("currentEvent", null);
    			eventMessage = "Next repetition: " + currentEvent.get().getEventTime();
    		}
    	} else {
    		model.addAttribute("currentEvent", null);
    		eventMessage = "Session already complete ";
    	}
    	model.addAttribute("eventMessage", eventMessage);
    	
    	return "sessions/sessionInfo";
    }
    
    // ---- new 
    @GetMapping("/new")
    public String newSession(@ModelAttribute("current") Session session) {
    	return "sessions/new";
    }
    
    @PostMapping("/new")
    public String create(@ModelAttribute("current") @Valid Session session, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "sessions/new";
		}
		
		int id = sessionService.save(session, getUser());
		String sessionDirLink = storageService.initDirectory(sessionService.buildPathStr(id)).toString();
		
		session.setLink(sessionDirLink);
		sessionService.update(id, session);
		
		return "redirect:/session/"+id;
    }
    
    
    // ---- edit
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("current", sessionService.getSessionsById(id));
        return "sessions/edit";
    }

    @PostMapping("/{id}/update")
    public String update(@ModelAttribute("current") @Valid Session session, BindingResult bindingResult, @PathVariable("id") int id) {
    	if (bindingResult.hasErrors()) {
            return "sessions/edit";
    	}
    	Session newSession = sessionService.getSessionsById(id);
    	newSession.setName(session.getName());
    	newSession.setDescription(session.getDescription());
    	sessionService.update(id, newSession);
        return "redirect:/session/"+id;
    }
    
    @PostMapping("/{id}/complete")
    public String complete(@PathVariable("id") int id, @RequestParam("eventId") int eventId) {
    	eventService.setComplete(eventId);
    	
    	return "redirect:/session/"+id;
    }
    
    // ---- delete
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
    	storageService.deleteDirectory(sessionService.getSessionsById(id).getLink());
    	sessionService.delete(id);
        return "redirect:/user";
    }
    
    private Person getUser() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        
        return personDetails.getPerson();
    }
}
