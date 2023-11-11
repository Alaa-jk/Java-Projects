package kickstart.terminplaner;

import jakarta.servlet.http.HttpSession;
import kickstart.leistungskatalog.Leistung;
import kickstart.leistungskatalog.LeistungService;
import kickstart.patientenverwaltung.PatientRepository;
import kickstart.personalverwaltung.Personal;
import kickstart.personalverwaltung.PersonalRepository;
import kickstart.rezepterfassung.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


@Controller
public class TerminplanerController {
private static final Logger LOG = LoggerFactory.getLogger(TerminplanerController.class);
	@Autowired
	private RezeptManagement rezeptManagement;
	@Autowired
	private LeistungInfoRepository leistungInfoRepository;
	@Autowired
	private PatientRepository patientRepository;
	@Autowired
	private LeistungService leistungService;
	@Autowired
	private TerminplanerRepository terminplanerRepository;
	@Autowired
	private PersonalRepository personalRepository;
	@Autowired
	private TerminService terminService;
	@Autowired
	private  RezeptRepository rezeptRepository;
	private static final String[] ROLES = new String[]{"boss","personal","therapeut"};

	/**
	 * Show the entry page for terminplaner
	 * @return entry page for terminplaner
	 */
	@GetMapping("/terminplanung")
	public String index() {
		return "terminplanung";
	}

	@GetMapping("/terminplanung/book")
	public String book(HttpSession session, Model model){
		session.removeAttribute("leistungId");
		session.removeAttribute("leistungen");
		session.removeAttribute("rezeptId");
		session.removeAttribute("beginSlotIndex");
		session.removeAttribute("count");
		session.removeAttribute("date");
		session.removeAttribute("therapistId");
		List<Rezept> rezepte = new LinkedList<>();
		List<Personal> therapists = new LinkedList<>();
		for(Rezept rezept : rezeptRepository.findAll()){
			if(rezept.getStatus() == 0){
				boolean reservationPossible = false;
				for(LeistungInfo li : rezept.getLeistungInfoList()){
					if(li.getMaxCount()-li.getActCount()-li.getOpenAppointmentsCount()>0)
						reservationPossible=true;
				}
				if(reservationPossible)
					rezepte.add(rezept);
			}
		}
		for(Personal therapist : personalRepository.getAllPersonal()){
			if(therapist.getRoles()!=null && therapist.getRoles().equals(ROLES[2]))
				therapists.add(therapist);
		}
		model.addAttribute("dateMax", LocalDate.now().plusDays(Termin.MAX_DAYS-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		session.setAttribute("rezepte", rezepte);
		session.setAttribute("therapists", therapists);
		return "terminplanung_book_date_input";
	}

	@PostMapping("/terminplanung/book")
	public String showPatientForm(
			@RequestParam("rezeptId") String rezeptId_str,
			@RequestParam("beginSlotIndex") String beginSlotIndex_str,
			@RequestParam("date") String date_str,
			@RequestParam("therapistId") String therapistId_str, HttpSession session){
		long rezeptId = Long.valueOf(rezeptId_str);
		List<Leistung> leistungen = new LinkedList<>();
		for(LeistungInfo leistungInfo : rezeptRepository.findById(rezeptId).orElseThrow().getLeistungInfoList()){
			if(leistungInfo.getMaxCount()-leistungInfo.getActCount()-leistungInfo.getOpenAppointmentsCount()>0)
				leistungen.add(leistungInfo.getLeistung());
		}
		session.setAttribute("leistungen",leistungen);
		session.setAttribute("rezeptId",rezeptId_str);
		session.setAttribute("beginSlotIndex",beginSlotIndex_str);
		session.setAttribute("date",date_str);
		session.setAttribute("therapistId",therapistId_str);
		return "terminplanung_eingabe_leistung";
	}
	@PostMapping("/terminplanung/book/leistung")
	public String toEingabeAnzahl(
			@RequestParam("leistungId") String leistungId_str, HttpSession session){
		long leistungId = Long.valueOf(leistungId_str);
		long rezeptId = Long.valueOf(session.getAttribute("rezeptId").toString());
		LeistungInfo leistungInfo = null;
		for(LeistungInfo li : rezeptManagement.findById(rezeptId).orElseThrow().getLeistungInfoList()){
			if(li.getLeistung().getId() == leistungId) {
				leistungInfo = li;
				break;
			}
		}
		if(leistungInfo != null)
			session.setAttribute("max", leistungInfo.getMaxCount()
					-leistungInfo.getActCount()-leistungInfo.getOpenAppointmentsCount());
		else
			session.setAttribute("max", 0);
		session.setAttribute("leistungId", leistungId_str);
		return "terminplanung_eingabe_anzahl";
	}

	@PostMapping(path = "/terminplanung/book/anzahl")
	String addEntry(HttpSession session, @RequestParam("count") String count) {

		int countInt = Integer.parseInt(count);
		LocalDate d = LocalDate.parse((CharSequence) session.getAttribute("date"),
				DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		Rezept rezept = rezeptRepository.findById(Long.parseLong(session.getAttribute("rezeptId").toString()))
				.orElseThrow();
		Personal therapist = personalRepository
				.findById(Long.parseLong((session.getAttribute("therapistId")).toString()))
				.orElseThrow();
		Leistung leistung = leistungService
				.findLeistungById(Long.parseLong(session.getAttribute("leistungId").toString()));

		Termin t = new Termin(rezept.getPatient(), therapist,
				rezept, leistung, 0,
				Integer.parseInt(session.getAttribute("beginSlotIndex").toString()), countInt, d);

		AtomicReference<String> msg = new AtomicReference<>("");
		if(!terminService.isWithinTimeslots(t) || terminService.hasCollision(t)){
			Map<LocalDate, Integer> map = terminService.recommend(t);
			if(map == null)
				msg.set("Kein Termin verfügbar: Versuchen Sie mit einer anderen Zeitdauer");
			else {
				for (Map.Entry<LocalDate, Integer> e : map.entrySet()) {
					msg.set("Alternativer Termin: "
							+ e.getKey().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
							+ " " + Termin.timeslots[e.getValue()]);
				}
			}
		}
		else {
			for(LeistungInfo li : rezept.getLeistungInfoList()){
				if(li.getLeistung().getId()==leistung.getId()) {
					if (li.getMaxCount() - li.getActCount() - countInt < 0) {
						msg.set("Buchung fehlgeschlagen: Es wurde maximal verbuchbare Anzahl überschritten");
						LOG.error("Es wurde versucht, mehr Leistungseinheiten zu verbuchen als verschrieben wurde");
						return "redirect:/terminplanung/book";
					}
					li.setOpenAppointmentsCount(li.getOpenAppointmentsCount() + countInt);
					leistungInfoRepository.save(li);
				}
			}
			rezeptManagement.save(rezept);
			terminplanerRepository.save(t);
			msg.set("Termin am "+t.getDate()+" um "+t.getTime()+" Uhr wurde reserviert");
		}
		session.setAttribute("msg", msg.toString());
		return "terminplanung_book_finish";
	}
	@GetMapping("/terminplanung/therapist_plan")
	public String showTherapistPlanForm(Model model){
		List<Personal> therapists = new LinkedList<>();
		for(Personal therapist : personalRepository.getAllPersonal()){
			if(therapist.getRoles()!=null && therapist.getRoles().equals(ROLES[2]))
				therapists.add(therapist);
		}
		model.addAttribute("dateMax", LocalDate.now().plusDays(Termin.MAX_DAYS-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		model.addAttribute("therapists",therapists);
		return "terminplanung_therapist_plan_date_input";
	}

	@PostMapping("/terminplanung/therapist_plan")
	public String showTherapistPlan(@RequestParam("date") String date_str,
									@RequestParam("therapistId") String therapistId_str,
									Model model){
		LocalDate date = null;
		Long therapistId = Long.valueOf(therapistId_str);
		List<Termin> appointments = new ArrayList<>();
		if(date_str.equals("")){
			for (Termin t : terminplanerRepository.findAll()) {
				if (t.getStatus() == 0 && t.getTherapist().getId() == therapistId)
					appointments.add(t);
			}
			Collections.sort(appointments, new Comparator<Termin>() {
				public int compare(Termin t1, Termin t2) {
					if (t1.getDate() == null || t2.getDate() == null)
						return 0;
					return t1.getDate().compareTo(t2.getDate());
				}
			});
		}
		else {
			date = LocalDate.parse(date_str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			for (Termin t : terminplanerRepository.findByDate(date)) {
				if (t.getStatus() == 0 && t.getTherapist().getId() == therapistId)
					appointments.add(t);
			}
		}
		model.addAttribute("appointments",appointments);
		model.addAttribute("date",date == null ? "" : date);
		return "terminplanung_therapist_plan";
	}

	@GetMapping("/terminplanung/all")
	public String showAppointmentsForm(Model model){
		model.addAttribute("dateMax", LocalDate.now().plusDays(Termin.MAX_DAYS-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		return "terminplanung_all_date_input";
	}

	@PostMapping("/terminplanung/all")
	public String showAppointments(@RequestParam("date") String date_str, Model model){
		LocalDate date = LocalDate.parse(date_str,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		List<Termin> appointments = new ArrayList<>();
		for(Termin t : terminplanerRepository.findByDate(date)){
			if(t.getStatus() == 0)
				appointments.add(t);
		}
		model.addAttribute("appointments",appointments);
		model.addAttribute("date",date);
		return "terminplanung_all";
	}

	/**
	 * Remove an appointment
	 * @param id
	 * @param session
	 * @return The same page with the updated appointment list
	 */
	@GetMapping(path = "/terminplaner_date_chosen/termin/{id}")
	String removeEntry(@PathVariable long id, HttpSession session) {

		return terminplanerRepository.findById(id).map(it -> {
			String msg = "Der Termin vom Patient "+
					it.getPatient().getFirstname()
							+" "+it.getPatient().getLastname()
							+" am "+it.getDate()+" um "+
					it.getTime()+" Uhr wurde gelöscht";
			it.getRezept().getLeistungInfoList().forEach(li -> {
				if (li.getLeistung().getId() == it.getLeistung().getId()){
					li.setOpenAppointmentsCount(li.getOpenAppointmentsCount() - it.getCountUnit());
				}
			});
			terminplanerRepository.delete(it);
			session.setAttribute("msg", msg);

			return "terminplanung_removed";

		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	/**
	 * Mark an appointment as completed (service was provided)
	 * @param id
	 * @param session
	 * @return The page for the same date as the current appointment
	 */
	@GetMapping(path = "/terminplaner_date_chosen/termin/{id}/complete")
	String markAsCompleted(@PathVariable long id, HttpSession session) {
		Termin t = terminplanerRepository.findById(id).orElseThrow();
		t.setStatus(1);
		t.getRezept().getLeistungInfoList().forEach(leistungInfo -> {
			if(leistungInfo.getLeistung().getId() == t.getLeistung().getId()) {
				leistungInfo.setActCount(leistungInfo.getActCount() + t.getCountUnit());
				leistungInfo.setOpenAppointmentsCount(leistungInfo.getOpenAppointmentsCount() - t.getCountUnit());
				leistungInfoRepository.save(leistungInfo);
			}
		});
		rezeptManagement.save(t.getRezept());
		terminplanerRepository.save(t);
		session.setAttribute("msg","Die Leisung für den Termin vom Patient "
				+ t.getPatient().getFirstname() + " " + t.getPatient().getLastname() +
				" am "+
				t.getDate()+" um "+t.getTime()+" Uhr wurde als erbracht markiert");
		return "terminplanung_removed";
	}
}
