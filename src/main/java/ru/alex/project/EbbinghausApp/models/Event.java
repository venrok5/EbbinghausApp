package ru.alex.project.EbbinghausApp.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import ru.alex.project.EbbinghausApp.util.ConstantsClass;
import ru.alex.project.EbbinghausApp.util.ExtendedDate;



@Entity
@Table(name = "Event")
public class Event implements Comparable<Event> {
	
	@Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int event_id;
	
	@ManyToOne
	@JoinColumn(name = "session_id", referencedColumnName = "session_id")
	private Session session;
	
    @Column(name = "eventtime")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = ConstantsClass.DATE_PATTERN)
    private Date eventTime;
	
	@Column(name = "iscomplete")
	private Boolean isComplete = false;
	
	@Transient
	private int positionNumber;
	
	@Transient
	private int index;
	
	public Event() {
	}
	
	public String toString() {
		return ExtendedDate.dateToString(this.getEventTime(), ConstantsClass.DATE_PATTERN);
	}
	
	public String displayTime() {
		return ExtendedDate.dateToString(this.getEventTime(), ConstantsClass.DATE_PATTERN_SHORT);
	}
	
	public static List<Event> makeEvents(Date startDate, Date endDate) {
		List<Event>newEvents = new ArrayList<>();
		List<Date> dates = calculateDatesList(startDate, endDate);
		
		for (Date date : dates) {
			Event event = new Event();
			event.setEventTime(date);
			newEvents.add(event);
		}
		return newEvents;
	}
	
	private static List<Date> calculateDatesList(Date startDate, Date endDate) {
		
		List<Date>newEvents = new ArrayList<>();
		
		newEvents.add(ExtendedDate.addHoursToDate(startDate, 1));
		newEvents.add(ExtendedDate.addHoursToDate(startDate, 9));
		
		double repeatitionInterval = 0;
		boolean doContinue = startDate.before(endDate); // TODO: validate on form
		
		while (doContinue) {
			
			repeatitionInterval = 2.5*repeatitionInterval + 1;
			
			int nextRepeatitionIncriment = (int)Math.floor(repeatitionInterval);
			
			Date event = ExtendedDate.addDaysToDate(startDate, nextRepeatitionIncriment);
			
			if (event.after(endDate)) {
				newEvents.add(ExtendedDate.addDaysToDate(endDate, -1));
				doContinue = false;
				break;
			}
			
			newEvents.add(event);
		}
		return newEvents;
	}

	@Override
	public int compareTo(Event event) {
		return eventTime.compareTo(event.eventTime);
	}
	
	public int getEvent_id() {
		return event_id;
	}

	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public Boolean getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}

	public int getPositionNumber() {
		return positionNumber;
	}

	public void setPositionNumber(int positionNumber) {
		this.positionNumber = positionNumber;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
