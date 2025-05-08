package ru.alex.project.EbbinghausApp.models;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import ru.alex.project.EbbinghausApp.util.ConstantsClass;
import ru.alex.project.EbbinghausApp.util.ExtendedDate;

@Entity
@Table(name = "Session")
public class Session implements Comparable<Session> {
	
	@Id
    @Column(name = "session_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int session_id;
	
	@ManyToOne
	@JoinColumn(name = "person_id", referencedColumnName = "person_id")
	private Person creator;
	
	@OneToMany(mappedBy = "session", cascade = CascadeType.PERSIST)
	private List<Event> events;

	
	//TODO: @NotEmpty(message = "Set starting date")
    @Column(name = "startdate")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = ConstantsClass.DATE_PATTERN)
    private Date startDate;
	
	//TODO: @NotEmpty(message = "Set ending date")
    @Column(name = "enddate")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = ConstantsClass.DATE_PATTERN)
    private Date endDate;
	
	@NotEmpty(message = "Title empty")
    @Size(min = 2, max = 100, message = "username must include at least 2 simbols")
    @Column(name = "name")
    private String name;
	
	@Size(max = 2048, message = "path should not contain more than 2048 characters")
	@Column(name = "link")
	private String link;
	
	@Size(max = 2048, message = "text should not contain more than 2048 characters")
	@Column(name = "description")
	private String description;
	
	@Size(max = 2048, message = "text should not contain more than 2048 characters")
	@Column(name = "data")
	private String data;
	
	public Session() {
	}
	
	
	public String whenStarted() {
		return ExtendedDate.dateToString(this.getStartDate(), ConstantsClass.DATE_PATTERN);
	}
	
	public String whenLastDate() {
		return ExtendedDate.dateToString(this.getEndDate(), ConstantsClass.DATE_PATTERN);
	}
	
	public String whenStartedDisplay() {
		return ExtendedDate.dateToString(this.getStartDate(), ConstantsClass.DATE_PATTERN_SHORT);
	}
	
	public String whenEndedDisplay() {
		return ExtendedDate.dateToString(this.getEndDate(), ConstantsClass.DATE_PATTERN_SHORT);
	}
	
	public int daysNumber() {
		return ExtendedDate.getNumberDaysBetween(this.getStartDate(), this.getEndDate());
	}
	
	public int eventsNumber() {
		return this.getEvents().size();
	}
	
	public int getSession_id() {
		return session_id;
	}

	public void setSession_id(int session_id) {
		this.session_id = session_id;
	}

	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public int compareTo(Session session) {
		return this.startDate.compareTo(session.startDate);
	}
}
