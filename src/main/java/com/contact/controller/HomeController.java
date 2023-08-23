package com.contact.controller;


import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contact.dao.PersonRepository;
import com.contact.helper.Message;
import com.contact.model.Person;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	

	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - ContactBook");
		return "home";
	}
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About - ContactBook");
		return "about";
	}
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "SignUp - ContactBook");
		model.addAttribute("person", new Person() );
		return "signup";
	}
	
	

	
	//handler for registering user
	
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("person") Person person,BindingResult result,@RequestParam(value = "agreement",defaultValue = "false") boolean agreement,Model model,HttpSession session) {
		
	try {
		if(!agreement) {
			System.out.println("You have not agreed the terms and codition");
			throw new Exception("You have not agreed the terms and codition");
		}
		if(result.hasErrors()) {
			model.addAttribute("person", person);
			System.out.println(result);
			return "signup";
		}
		person.setRole("Role_USER");
		person.setEnabled(true);
		
		person.setImageUrl("default.jpg");
		person.setPassword(passwordEncoder.encode(person.getPassword()));
		System.out.println("Agrrement"+ agreement);
		this.personRepository.save(person);
		model.addAttribute("person", new Person());
		session.setAttribute("message", new Message("Succesfully Registerd !!", "alert-success"));
		return "signup";
	} catch (Exception e) {
		model.addAttribute("person", person);
		session.setAttribute("message", new Message("Something Went Wrong !!" + e.getMessage(), "alert-danger"));
		e.printStackTrace();
		return "signup";
	}
		
	}
	
	@GetMapping("/signin")
	public String customLogin(Model model) {
		model.addAttribute("title", "Login - ContactBook");
		return "login";
	}

}
