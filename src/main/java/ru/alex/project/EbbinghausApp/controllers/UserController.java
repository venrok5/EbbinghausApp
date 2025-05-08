package ru.alex.project.EbbinghausApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import ru.alex.project.EbbinghausApp.models.Person;
import ru.alex.project.EbbinghausApp.models.Session;
import ru.alex.project.EbbinghausApp.security.PersonDetails;
import ru.alex.project.EbbinghausApp.services.FilesStorageService;
import ru.alex.project.EbbinghausApp.services.PersonService;

@Controller
@RequestMapping("/user")
public class UserController {
	private final PersonService personService;
	private final FilesStorageService storageService;

    @Autowired
    public UserController(PersonService personService, FilesStorageService storageService) {
        this.personService = personService;
        this.storageService = storageService;
    }

    @GetMapping()
    public String showUserInfo(Model model) {  
        Person user = getUser();
        int id = user.getPerson_id();
        
        model.addAttribute("user", user);
        model.addAttribute("sessions", personService.getSessionsForPersonWithId(id)); 

        return "user/index";
    }
    
    
    // ---- edit
    @GetMapping("/update")
    public String edit(Model model) {
    	model.addAttribute("user", getUser()); 
    	return "user/edit";
    }
    
    @PostMapping("/update")
    public String update(@ModelAttribute("user") @Valid Person user, BindingResult bindingResult) {
    	
    	if (bindingResult.hasErrors()) {
    		return "user/edit";
    	}
		user.setPassword(getUser().getPassword());
    	personService.update(getUser().getPerson_id(), user);
    	return "redirect:/user";
    }

    
    // ----- delete
    @PostMapping("/delete")
    public String deleteUser() {
    	int id = getUser().getPerson_id();
    	for (Session session : personService.getSessionsForPersonWithId(id)) {
    		String link = session.getLink();
    		if (!link.isEmpty()) {
    			storageService.deleteDirectory(link);
    		}
		}
    	personService.delete(id);
    	return "redirect:/logout";
    }
    
    private Person getUser() { // logined user
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        
        return personDetails.getPerson();
    }
   
}
