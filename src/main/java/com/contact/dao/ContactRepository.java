package com.contact.dao;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contact.model.Contact;
import com.contact.model.Person;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

	@Query("from Contact as c where c.person.id = :personId")
	public Page<Contact> findContactsByPerson(@Param("personId") int personId,Pageable pageable);
	
	
	//search....
	public List<Contact> findByNameContainingAndPerson(String name, Person person);
}
