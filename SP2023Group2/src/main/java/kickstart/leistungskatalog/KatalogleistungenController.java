package kickstart.leistungskatalog;


import java.util.LinkedList;
import java.util.List;


import kickstart.rezepterfassung.LeistungInfoRepository;
import kickstart.rezepterfassung.RezeptManagement;
import kickstart.rezepterfassung.RezeptRepository;
import kickstart.terminplaner.TerminplanerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;


@Transactional
@EnableTransactionManagement
@SessionAttributes("successMessage")
@SuppressWarnings({"unused"})
@Controller
@RequestMapping("/Katalogleistungen")
public class KatalogleistungenController {

	//
	@Autowired private LeistungInfoRepository leistungInfoRepository;
	@Autowired private RezeptRepository rezeptRepository;
	@Autowired private TerminplanerRepository terminplanerRepository;
	@Autowired private RezeptManagement rezeptManagement;


	private final LeistungService leistungService;

	@Autowired
	public KatalogleistungenController(LeistungService leistungService) {
		this.leistungService = leistungService;
	}

//    @GetMapping
//    public List<Leistung> getAllLeistungen() {
//        return leistungService.getAllLeistungen();
//    }


	@GetMapping
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	public String getAllLeistungen(Model model) {
		model.addAttribute("LeistungList",leistungService.getAllLeistungen());

		return "katalogleistungen";


}



//	@GetMapping("/{id}")
//	public void getLeistungById(@PathVariable long id) {
//		return leistungService.findLeistungById(id);
//	}

//	@GetMapping("/Katalogleistungen")
//	public String index() {
//		return "katalogleistungen";
//	}

	@GetMapping("/LeistungDelete/{id}")
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	public String leistungdelete(Model model,@PathVariable Long id,RedirectAttributes redirectAttributes) {

		System.out.println(id);
		redirectAttributes.addFlashAttribute("messageDelete", "Gelöscht");
		return rezeptManagement.deleteLeistung(id);
		/*
		System.out.println("Hello");
		try {
			
			leistungService.deleteLeistungById(id);
			redirectAttributes.addFlashAttribute("messageDelete", "Gelöscht");
		} catch (Exception e) {
			System.out.print(e);
		}

		return "redirect:/Katalogleistungen";*/
	}


	@GetMapping("/AddLeistung")
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	public String AddLeistung() {

		return "addLeistung";
	}


	@PostMapping("/AddLeistung")
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	public String createLeistungen(@Validated Leistung leistung, Errors result, RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "addLeistung";
		}

		try {

			leistungService.createLeistung(leistung);

			redirectAttributes.addFlashAttribute("message", "Erfolgreich!");

		} catch (Exception e) {
			System.out.print(e);
		}
		return "redirect:/Katalogleistungen";
	}





	@GetMapping("/updateleistung/{id}")
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	public String updateleistung(Model model,@PathVariable Long id) {

		model.addAttribute("leistung",leistungService.findLeistungById(id));


		return "updateleistung";
	}


	@PostMapping("/updateleistung")
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	public String updateleistungaction(@Validated Leistung leistung, Errors result,RedirectAttributes redirectAttributes) {
		System.out.println(leistung.getId());
		Leistung leistunglocal =leistungService.findLeistungById(leistung.getId())	;

		//Check if the service already exists -> if y then error message
		if(rezeptManagement.existsPrescriptionForLeistung(leistung.getId())
		&& Math.ceil(leistung.getPrise()) != Math.ceil(leistunglocal.getPrise()))
			return "redirect:/error/updateLeistung/Msg/"+leistung.getId();

		leistunglocal.setName(leistung.getName());
		leistunglocal.setPrise(leistung.getPrise());
		leistunglocal.setDauer(leistung.getDauer());
		leistungService.updateLeistung(leistunglocal);
		redirectAttributes.addFlashAttribute("message", "Erfolgreich aktualisiert.");

		return "redirect:/Katalogleistungen";
	}

}