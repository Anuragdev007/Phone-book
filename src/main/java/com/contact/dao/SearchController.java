package com.contact.dao;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.contact.model.Contact;
import com.contact.model.Person;

@RestController
public class SearchController {
	
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	//search handler
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal){
		
		System.out.println(query);
		
		Person person = this.personRepository.getPersonByPersonName(principal.getName());
		
		List<Contact> contacts = this.contactRepository.findByNameContainingAndPerson(query, person);
		return ResponseEntity.ok(contacts);
	}

}
