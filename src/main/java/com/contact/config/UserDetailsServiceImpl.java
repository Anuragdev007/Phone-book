package com.contact.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.contact.dao.PersonRepository;
import com.contact.model.Person;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private PersonRepository personRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		Person person = personRepository.getPersonByPersonName(username);
		
		
		if(person==null)
		{
			throw new UsernameNotFoundException("Could not found user");
		}
		CustomUserDetail customUserDetail=new CustomUserDetail(person);
		return customUserDetail;
	}






}
