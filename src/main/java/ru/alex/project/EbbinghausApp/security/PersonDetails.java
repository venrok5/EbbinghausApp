package ru.alex.project.EbbinghausApp.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ru.alex.project.EbbinghausApp.models.Person;

public class PersonDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	private final Person person;

    public PersonDetails(Person person) {
        this.person = person;
    }
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		return this.person.getPassword();
	}

	@Override
	public String getUsername() {
		return this.person.getUsername();
	}
	
	@Override
	public boolean isAccountNonExpired() {
	    return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
    public boolean isCredentialsNonExpired() {
	    return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public Person getPerson() { // authorized user data 
        return this.person;
    }
}
