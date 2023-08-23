package com.contact.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.contact.model.Person;

public class CustomUserDetail implements UserDetails {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2913700274180739060L;
	private Person person;
	
	
	
	public CustomUserDetail(Person person) {
		super();
		this.person = person;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	
		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(person.getRole());
		return List.of(simpleGrantedAuthority);
	}

	@Override
	public String getPassword() {
		
		return person.getPassword();
	}

	@Override
	public String getUsername() {
		
		return person.getEmail();
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

	
}
