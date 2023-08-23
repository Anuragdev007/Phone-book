package com.contact.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.contact.model.Person;



public interface PersonRepository extends JpaRepository<Person, Integer> {

	
	@Query("select p from Person p where p.email = :email")
	public Person getPersonByPersonName(@Param("email") String email);
	
	
	
	Optional<Person> findPersonByEmail(String email);
	
	
}
