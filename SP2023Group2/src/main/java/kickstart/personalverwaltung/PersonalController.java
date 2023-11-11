package kickstart.personalverwaltung;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import kickstart.rezepterfassung.RezeptManagement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import kickstart.roles.RolesRepo;
import lombok.experimental.StandardException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller

public class PersonalController {

	@Autowired
	private RezeptManagement rezeptManagement;
	@Autowired
	private PersonalService personalService;
	@Autowired
	private  PasswordEncoder passwordEncoder;
	@Autowired
	private RolesRepo rolesRepo;
	
	public PersonalController(PersonalService personalService,PasswordEncoder passwordEncoder,RolesRepo rolesRepo) {
		super();
		this.personalService = personalService;
		this.passwordEncoder= passwordEncoder;
		this.rolesRepo= rolesRepo;
	}

	@GetMapping("/personal")
	@PreAuthorize("hasAuthority('boss')")
	public String listPersonal(Model model) {
		model.addAttribute("personals", personalService.getAllPersonals());
		
		return "personal";
	}
	
	
	@GetMapping("/login")
	public String login() {
	
		return "login";
	}
	

	@GetMapping("/personal/new")
	@PreAuthorize("hasAuthority('boss')")
	public String createPersonalForm(Model model) {
		
	
		
		Personal personal = new Personal();
		model.addAttribute("personal", personal);
		model.addAttribute("roles", rolesRepo.getAllRole());
		return "personalNew";
	}
	
	@PostMapping("/personal")
	@PreAuthorize("hasAuthority('boss')")
	public String savePersonal(@ModelAttribute("personal") Personal personal) {
		personal.setPassword(passwordEncoder.encode(personal.getPassword()));
		personalService.savePersonal(personal);
		return "redirect:/personal";
	}
	
	@GetMapping("/personal/edit/{id}")
	@PreAuthorize("hasAuthority('boss')")
	public String editPersonalForm(@PathVariable Long id, Model model) {
		model.addAttribute("personal", personalService.getPersonalById(id));
		return "personalEdit";
	}
	
	@PostMapping("/personal/{id}")
	@PreAuthorize("hasAuthority('boss')")
	public String updatePersonal(@PathVariable Long id,
			@ModelAttribute("personal") Personal personal,
			Model model) {
		
		Personal existingPersonal = personalService.getPersonalById(id);
		existingPersonal.setId(id);
		existingPersonal.setFirstname(personal.getFirstname());
		existingPersonal.setLastname(personal.getLastname());
		existingPersonal.setFullOrParttime(personal.getFullOrParttime());
		existingPersonal.setPosition(personal.getPosition());
		existingPersonal.setAddress(personal.getAddress());
		existingPersonal.setBirthdate(personal.getBirthdate());
		existingPersonal.setUsername(personal.getUsername());
		existingPersonal.setPassword(passwordEncoder.encode(personal.getPassword()));
		existingPersonal.setSalary(personal.getSalary());
		existingPersonal.setIban(personal.getIban());
		
		personalService.updatePersonal(existingPersonal);
	
		return "redirect:/personal";		
	}
	
	@GetMapping("/personal/{id}")
	@PreAuthorize("hasAuthority('boss')")
	public String deletePersonal(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		if(id == 1){
			redirectAttributes.addFlashAttribute("messageDelete", "Kann nicht Gelöscht werden..");
			return "redirect:/personal";

		}
		/*
		personalService.deletePersonalById(id);
		return "redirect:/personal";*/
		return rezeptManagement.deletePersonal(id);
	}
	
	

}

