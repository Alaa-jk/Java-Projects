package kickstart.patientenverwaltung;

import kickstart.rezepterfassung.RezeptManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class PatientController {
	//RezeptManagement for the check functionality by deleting of patients
	@Autowired
	private RezeptManagement rezeptManagement;

	private PatientService patientService;
	


	public PatientController(PatientService patientService) {
		super();
		this.patientService = patientService;
	
	}


	@GetMapping("/patient")
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	public String listPatient(Model model) {
		model.addAttribute("patients", patientService.getAllPatients());
		return "patient";
	}
	
	@GetMapping("/patient/new")
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	public String createPatientForm(Model model) {
		
		
		Patient patient = new Patient();
		model.addAttribute("patient", patient);
		return "patientNew";
	}
	
	@PostMapping("/patient")
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	public String savePatient(@ModelAttribute("patient") Patient patient) {
	
		patientService.savePatient(patient);
		return "redirect:/patient";
	}
	
	@GetMapping("/patient/edit/{id}")
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	public String editPatientForm(@PathVariable Long id, Model model) {
		model.addAttribute("patient", patientService.getPatientById(id));
		return "patientEdit";
	}
	
	@PostMapping("/patient/{id}")
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	public String updatePatient(@PathVariable Long id,
		@ModelAttribute("patient") Patient patient,
		Model model) {
		
		Patient existingPatient = patientService.getPatientById(id);
		existingPatient.setId(id);
		existingPatient.setFirstname(patient.getFirstname());
		existingPatient.setLastname(patient.getLastname());
		existingPatient.setHealthInsuranceNumber(patient.getHealthInsuranceNumber());
		existingPatient.setHealthInsurance(patient.getHealthInsurance());
		existingPatient.setAddress(patient.getAddress());
		existingPatient.setBirthdate(patient.getBirthdate());
		
		patientService.updatePatient(existingPatient);
		return "redirect:/patient";		
	 }
	
	@GetMapping("/patient/{id}")
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	public String deletePatient(@PathVariable Long id) {
		/*//DEV: Check if a patient can be deleted safely
		patientService.deletePatientById(id);
		return "redirect:/patient";
		 */
		return rezeptManagement.deletePatient(id);
	}
	
	

}
