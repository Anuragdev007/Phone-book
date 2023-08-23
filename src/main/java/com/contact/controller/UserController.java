
package com.contact.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contact.dao.ContactRepository;
import com.contact.dao.PersonRepository;
import com.contact.helper.Message;
import com.contact.model.Contact;
import com.contact.model.Person;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private ContactRepository contactRepository;

	@ModelAttribute
	public void addCommonData(org.springframework.ui.Model model, Principal principal) {
		String userName = principal.getName();

		Person person = personRepository.getPersonByPersonName(userName);

		model.addAttribute("person", person);
	}

	@GetMapping("/index")

	public String dashboard(org.springframework.ui.Model model, Principal principal) {

		return "normal/user_dashboard";
	}

	@GetMapping("/success")
	public String success(org.springframework.ui.Model model, Principal principal) {

		return "normal/success";

	}

	// open add form handler

	@GetMapping("/add-contact")
	public String openAddContactForm(org.springframework.ui.Model model) {
		model.addAttribute("title", "Add Contact - ContactBook");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact_form";
	}
	
	//processing add contact form
	
	@PostMapping("/process-contact")
	public String processContact( 
			@ModelAttribute("contact") Contact contact,
			@RequestParam("profileImg") MultipartFile file,
			Principal principal,HttpSession session) {
		try {

			String name = principal.getName();
			Person person = this.personRepository.getPersonByPersonName(name);
			
			//uploading file....
			if(file.isEmpty()) {
				contact.setImageName("user.png");
				
			}else {
				contact.setImageName(file.getOriginalFilename());
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			
			
			contact.setPerson(person);
			person.getContacts().add(contact);
			this.personRepository.save(person);
			
			System.out.println("Data"+ contact);
			
			session.setAttribute("message", new Message("Yor contact is addedd !!", "success"));
			
			
			
		} catch (Exception e) {
			System.out.println("Error"+e.getMessage());
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong Try again.... !!", "danger"));
			
		}
		return "normal/add_contact_form";
		
	}
	
	//show contacts handler
	
	@GetMapping("/show-contact/{page}")
	public String showContacts(@PathVariable("page") Integer page,org.springframework.ui.Model m,Principal principal) {
		m.addAttribute("title", "All contacts - ContactBook");
		
		String userName = principal.getName();
	Person user = this.personRepository.getPersonByPersonName(userName);
 Pageable pageable=	PageRequest.of(page, 5);
	Page<Contact> contacts = this.contactRepository.findContactsByPerson(user.getId(),pageable);
	m.addAttribute("contacts", contacts);
	m.addAttribute("currentPage", page);
	m.addAttribute("totalPages", contacts.getTotalPages());
		return "normal/show_contacts";
	}
	
	
	//showing particular contact detail
	
	@GetMapping("/{cid}/contact")
	public String showContactDetail(@PathVariable("cid") Integer id,org.springframework.ui.Model m,Principal principal) {
		Optional<Contact> findById = this.contactRepository.findById(id);
		Contact contact = findById.get();
		String userName = principal.getName();
		Person person = this.personRepository.getPersonByPersonName(userName);
		if(person.getId()==contact.getPerson().getId()) {
			m.addAttribute("contact", contact);
		}
		
		
		return "normal/contact_detail";
	}
	
	//delete contact handler
	
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer id,org.springframework.ui.Model mode,HttpSession session) {
		Optional<Contact> contactOptional = this.contactRepository.findById(id);
		Contact contact = contactOptional.get();
		contact.setPerson(null);
		this.contactRepository.delete(contact);
		session.setAttribute("message", new Message("Contact deleted successfully.....", "success"));
		return"redirect:/user/show-contact/0";
		
	}
	
	//open update form handler
	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid, org.springframework.ui.Model m) {
		m.addAttribute("title", "Update Contact");
		
		Contact contact = this.contactRepository.findById(cid).get();
		m.addAttribute("contact", contact);
		
		return "normal/update_form";
	}
	
	//update contact handler
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute Contact contact,@RequestParam("profileImg") MultipartFile file,Model m,HttpSession session,Principal principal) {
		
		try {
			//old contact details
			Contact oldContactDetail = this.contactRepository.findById(contact.getCid()).get();
			
			
			//image
			if(!file.isEmpty()) {
				//rewrite the file
				//delete old photo
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file1=new File(deleteFile,oldContactDetail.getImageName());
				file1.delete();
				//update new photo
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImageName(file.getOriginalFilename());
			}else {
				contact.setImageName(oldContactDetail.getImageName());
			}
			
			Person person = this.personRepository.getPersonByPersonName(principal.getName());
			contact.setPerson(person);
			
			this.contactRepository.save(contact);
			session.setAttribute("message", new Message("Your Contact updated successfully....", "success"));
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return"redirect:/user/"+contact.getCid()+"/contact";
	}
	
	//your profile handler
	
	@GetMapping("/profile")
	public String yourprofile() {
		
		return "normal/profile";
		
	}
	

}
