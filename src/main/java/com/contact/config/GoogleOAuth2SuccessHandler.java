package com.contact.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.contact.dao.ContactRepository;
import com.contact.dao.PersonRepository;
import com.contact.model.Person;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {
	
	
	@Autowired
	private PersonRepository personRepository;
	

	private RedirectStrategy redirectStrategy =new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		OAuth2AuthenticationToken token=(OAuth2AuthenticationToken) authentication;
		String email = token.getPrincipal().getAttributes().get("email").toString();

if(personRepository.findPersonByEmail(email).isPresent()) {



			
		}else {
			Person person = new Person();
			person.setName(token.getPrincipal().getAttributes().get("name").toString());
			person.setRole("Role_USER");
			person.setEnabled(true);
			person.setEmail(email);
			
			
			person.setImageUrl("default.jpg");
			personRepository.save(person);
			
		}
redirectStrategy.sendRedirect(request, response, "/user/success");
	}

}
