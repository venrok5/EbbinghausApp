package ru.alex.project.EbbinghausApp.models;

import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
@Table(name = "Person")
public class Person {

	@Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int person_id;
    
	@OneToMany(mappedBy = "creator", cascade = CascadeType.PERSIST)
    private List<Session> sessions;

	@NotEmpty(message = "username empty")
	@Size(min = 2, max = 100, message = "username must include at least 2 simbols")
	@Column(name = "username")
    private String username;

	@Column(name = "password")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{8,}$", message = "Password must contain one digit from 1 to 9, one lowercase letter, one uppercase letter, one special character, no space, and it must be at least 8 characters long")
    private String password;

	@Column(name = "email", unique=true)
    //TODO: @Pattern(regexp = "[AZ 0-9 ._%+ -]+@[AZ 0-9 . -]+\\.[AZ]{2,}", message = "invalid email form")
    private String email;
    
    public Person() {
    }

	public int getPerson_id() {
		return person_id;
	}

	public void setPerson_id(int person_id) {
		this.person_id = person_id;
	}

	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    	
}