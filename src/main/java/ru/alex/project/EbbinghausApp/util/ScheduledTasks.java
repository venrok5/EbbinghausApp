package ru.alex.project.EbbinghausApp.util;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ru.alex.project.EbbinghausApp.models.Event;
import ru.alex.project.EbbinghausApp.services.EventService;

@Component
@EnableAsync
public class ScheduledTasks {
	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	
	@Autowired
	public EventService eventService;
	
	@Autowired
    EmailService emailService;

	@Scheduled(cron = ConstantsClass.remindSchedule) 
	@Async
	public void reportCurrentTime() {
		List<Event>tasks = eventService.getEventsBeforeDate(new Date());

		for (Event event : tasks) {
			String email = eventService.getCreatorEmail(event);
			String sessionTitle = eventService.getSessionTitle(event);
			String username = eventService.getCreatorsName(event);
			sendMessage(email, sessionTitle, username);
		}
		
	}
	
	private void sendMessage(String email, String name, String username) {
    	try {
            emailService.sendSimpleEmail(email, "Reminder message", username+", we lovely inform you that today is a review day, when you need to refresh your knowledge. Session '"+name+"'.\n");
        } catch (MailException mailException) {
        	logger.error("Email sending error: "+mailException);
        }
    }
}	
