package kickstart.rezepterfassung;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpSession;
import kickstart.leistungskatalog.Leistung;
import kickstart.leistungskatalog.LeistungService;
import kickstart.patientenverwaltung.Patient;
import kickstart.patientenverwaltung.PatientRepository;
import kickstart.patientenverwaltung.PatientServiceImpl;
import kickstart.personalverwaltung.Personal;
import kickstart.personalverwaltung.PersonalServiceImpl;
import kickstart.terminplaner.Termin;
import kickstart.terminplaner.TerminService;
import kickstart.terminplaner.TerminplanerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class RezeptController {
	private static final Logger LOG = LoggerFactory.getLogger(RezeptController.class);
	@Autowired
	private TerminService terminService;
	@Autowired
	private TerminplanerRepository terminplanerRepository;
	@Autowired
	private LeistungInfoRepository leistungInfoRepository;
	@Autowired
	private RezeptRepository rezeptRepository;
	//DEV
	@Autowired
	private RezeptManagement rezeptManagement;
	@Autowired
	private LeistungService leistungService;
	@Autowired
	private PatientRepository patientRepository;
	@Autowired
	private PatientServiceImpl patientServiceImpl;
	@Autowired
	private PersonalServiceImpl personalServiceImpl;
	@Autowired
	private AbrechnungEntryRepository abrechnungEntryRepository;

	// All health insurances registered
	public static final String[] healthInsurances = new String[]{
			"Techniker Krankenkasse", "AOK", "Bayerische", "BARMER", "DAK"
	};
        // All available Roles
        private static final String[] ROLES = new String[]{"boss","personal","therapeut"};

	/**
	 * Show the list of the Rezept entries (not closed) sorted by health insurances with the total fee
	 * @param session
	 * @return All Rezept entries
	 */
	@GetMapping("/rezepte")
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	public String serveList(HttpSession session) {
		List<Rezept> rezepte = new LinkedList<>();
		rezeptRepository.findAll()
						.forEach(r -> { if(r.getStatus() == 0) rezepte.add(r); });
		session.setAttribute("rezepte", rezepte);
		session.setAttribute("hiNames", healthInsurances);
		return "rezepte";
	}
	/**
	 * Show the form to create a Rezept entry with a look-up for patient ids
	 * @param session
	 * @return Form to create a Rezept
	 */
	@GetMapping("/rezepte/rezeptNew")
	@PreAuthorize("hasAnyAuthority('boss','personal')")

	public String serveForm(HttpSession session) {
		Map<String, String> patientMap = new HashMap<>();
		Map<String, String> personalMap = new HashMap<>();
		for(Patient patient : patientRepository.findAll()){
			patientMap.put(patient.getId().toString(), patient.getLastname().toString());
		}
		for(Personal personal : personalServiceImpl.getAllPersonals()){
                    if( personal.getRoles() != null
                        && personal.getRoles().equals(ROLES[1]))//Show only reception staffs
			personalMap.put(personal.getId().toString(), personal.getLastname());
		}
		session.setAttribute("patientMap", patientMap);
		session.setAttribute("personalMap", personalMap);
		return "rezeptNew";
	}

	@GetMapping("/rezepte/{id}/abrechnen")
	@PreAuthorize("hasAnyAuthority('boss','personal')")

	public String settlePrescription(HttpSession session,
									 @PathVariable("id") String rezeptId){
		return rezeptManagement.closePrescription(Long.valueOf(rezeptId));
	}

	//Do only nessesally updates for AbrechnungEntries for each healthcare page
	public void updateAbrechnungEntries(){
		for(AbrechnungEntry abrechnungEntry: abrechnungEntryRepository.findAll()) {
			if (!abrechnungEntry.isArchived()) {
				//Update information

				abrechnungEntry.setPatientFirstname(
						patientRepository.findById(abrechnungEntry.getPatientId()).orElseThrow().getFirstname()
				);
				abrechnungEntry.setPatientLastname(
						patientRepository.findById(abrechnungEntry.getPatientId()).orElseThrow().getLastname()
				);
				abrechnungEntry.setPersonalFirstname(
						personalServiceImpl.getPersonalById(abrechnungEntry.getPersonalId()).getFirstname()
				);
				abrechnungEntry.setPersonalLastname(
						personalServiceImpl.getPersonalById(abrechnungEntry.getPersonalId()).getLastname()
				);
				abrechnungEntry.setLeistungName(
						leistungService.findLeistungById(abrechnungEntry.getLeistungId()).getName()
				);
				abrechnungEntry.setHealthInsurance(
						patientRepository.findById(abrechnungEntry.getPatientId()).orElseThrow().getHealthInsurance()
				);
				abrechnungEntryRepository.save(abrechnungEntry);
			}
		}
	}

	/**
	 * Show the List for the given health insurance
	 * @param session
	 * @param hiName
	 * @return List of finished prescriptions for the given health insurance with the total fee
	 */
	@GetMapping("/rezepte/health_insurance/{hiName}")
	String showEntriesForHI(HttpSession session, @PathVariable("hiName") String hiName){
		session.setAttribute("hiName", hiName);
		updateAbrechnungEntries();
		long totalSum = 0;
		Map<List<AbrechnungEntry>,Long> abrechnungEntries = new HashMap<>();
		List<Long> list = new ArrayList<>();
		for(AbrechnungEntry a : abrechnungEntryRepository.findAll()) {
			list.add(a.getRezeptId());
		}
		list = list.stream().distinct().sorted().toList();
		for(long rezeptId : list) {
			List<AbrechnungEntry> rezeptList = new ArrayList<>();
			long sum = 0;
			for(AbrechnungEntry abrechnungEntry: abrechnungEntryRepository.findByHealthInsurance(hiName)){
				if(rezeptId == abrechnungEntry.getRezeptId()) {
					if(abrechnungEntry.isServiceProvided()) {
						rezeptList.add(abrechnungEntry);
						sum += abrechnungEntry.getLeistungPrice()*abrechnungEntry.getLeistungCount();
					}
				}
			}
			if(rezeptList.size()!=0)
				abrechnungEntries.put(rezeptList,sum);
			totalSum += sum;
		}
		session.setAttribute("rezepteFinished", abrechnungEntries);
		session.setAttribute("totalSum", totalSum);
		return "krankenkasseDetail";
	}

	/**
	 * Format AtomicInteger for EUR with colon
	 * @param sum
	 * @return Price amount formatted
	 */
	public String formatEuroWithColon(AtomicInteger sum){
		return (sum.intValue() / 100) + "," + ((sum.intValue() % 100)==0?"00":(sum.intValue() % 100));
	}

	/**
	 * Add a new Rezept entry
	 * @param reqParam patient information, patientId and personalId
	 * @param session
	 * @return Forward to the order input form
	 */
	@RequestMapping(value="/rezepte/rezeptNew", method=RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	public String fillPatientInformation(@RequestParam Map<String, String> reqParam,
										 HttpSession session){

		//Patient for whom a prescription will be written
		Patient patient = patientRepository
				.findById(Long.parseLong(reqParam.get("patientId")))
				.orElseThrow();
		//The staff who writes the prescription for the patient
		Personal personal
				= personalServiceImpl
				.getPersonalById(Long.parseLong(reqParam.get("personalId").toString()));
		List<LeistungInfo> leistungInfoList
				= new LinkedList<>();
		//Create a Rezept with the input information
		Rezept rezept = new Rezept(patient, personal,
				leistungInfoList, 0,
				reqParam.get("approvalNumber"),
				reqParam.get("diagnosisNumber"),
				reqParam.get("province"),
				reqParam.get("personalGroup")
		);
		rezeptRepository.save(rezept);
		Iterable<Leistung> services = leistungService.findAll();
		session.setAttribute("services", services);
		session.setAttribute("rid", rezept.getId());
		return "rezeptInfo";
	}

	/**
	 * Create and attach LeistungInfo-Objects to the prescription
	 * @param session
	 * @param reqParam
	 * @return The same form for the additional LeistungInfo-Objects
	 */
	@RequestMapping(value="/rezepte/rezeptNew/rezeptInfo", method=RequestMethod.POST)
	public String fillLeistungInfo(HttpSession session,
	@RequestParam Map<String, String> reqParam){
		//DEV
		rezeptManagement.addLeistungInfoToList(
				Long.parseLong(session.getAttribute("rid").toString()),
				Long.parseLong(reqParam.get("serviceId")),
				Integer.parseInt(reqParam.get("countService"))
				);

		Iterable<Leistung> services = leistungService.findAll();
		session.setAttribute("services", services);
		return "rezeptInfo";
	}

	/**
	 * Delete a Rezept entry
	 * @param entry
	 * @return Back to the entry page for Rezepterfassung
	 */
	@GetMapping(path = "/rezepte/delete/{entry}")
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	String removeEntry(@PathVariable("entry") Optional<Rezept> entry) {
		//DEV
		return entry.map(it -> {
			return rezeptManagement.deletePrescription(it);
		}).orElseThrow();
	}

	/**
	 * Remove an OrderLine from an Order for the Rezept
	 * @param rezeptId
	 * @return Back to the same update form
	 */
	@GetMapping(path = "/rezepte/{id1}/delete/{id2}")
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	String removeService(@PathVariable("id1") String rezeptId,
						 @PathVariable("id2") Optional<LeistungInfo> leistungInfo) {

		Rezept rezept = rezeptRepository.findById(Long.parseLong(rezeptId))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return leistungInfo.map(
				li -> {
					rezept.getLeistungInfoList().remove(li);
					rezeptRepository.save(rezept);
					leistungInfoRepository.delete(li);
					return "redirect:/rezepte/"+rezeptId;
				}
		).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	/**
	 * Show the update form for service entries for the Rezept entry
	 * @param id Id of the Rezept entry
	 * @param session
	 * @return Update form for service entries for the Rezept
	 */
	@GetMapping("/rezepte/{id}")
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	public String showServicesUpdateForm(HttpSession session, @PathVariable("id") String id) {
		Rezept rezept = rezeptRepository.findById(Long.parseLong(id))
				.orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
		session.setAttribute("rezept", rezept);
		Iterable<Leistung> services = leistungService.findAll();
		session.setAttribute("services", services);

		return "rezeptDetail";
	}

	/**
	 * Add a LeistungInfo-Object to the prescription
	 * @param reqParam All parameter from the form
	 * @param session
	 * @param id Id of the Rezept entry
	 * @return Back to the same update form
	 */
	@RequestMapping(value="/rezepte/{id}/leistung", method=RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	String addService(@RequestParam Map<String, String> reqParam, HttpSession session,
					  @PathVariable("id") String id){
		Rezept rezept = rezeptRepository.findById(Long.parseLong(id))
				.orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
		long leistungId = Long.parseLong(reqParam.get("serviceId"));
		AtomicBoolean contains = new AtomicBoolean(false);
		rezept.getLeistungInfoList().forEach(li -> {
			//Increment setMaxCount if an entry should be created for an existing Leistungsart
			if(li.getLeistung().getId() == leistungId) {
				li.setMaxCount(li.getMaxCount()+Integer.parseInt(reqParam.get("countService")));
				leistungInfoRepository.save(li);
				contains.set(true);
			}
		});//If not create a new entry for LeistungInfo for the prescription
		if(!contains.get()){
			LeistungInfo leistungInfo =
					new LeistungInfo(leistungService.findLeistungById(leistungId),
							Integer.parseInt(reqParam.get("countService")),0,0);
			leistungInfoRepository.save(leistungInfo);
			rezept.getLeistungInfoList().add(leistungInfo);
		}
		rezeptRepository.save(rezept);
		session.setAttribute("rezept",rezept);
		return "redirect:/rezepte/"+id;
	}

	/**
	 * Update a Rezept entry
	 * @param reqParam All parameters from the form
	 * @param reqParam
	 * @return Back to the home page for Rezepterfassung
	 */

	@RequestMapping(value="/rezepte/{id}/rezeptinfo", method=RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	String updateEntry(@RequestParam Map<String, String> reqParam,
					   @PathVariable String id){

		return rezeptRepository.findById(Long.parseLong(id))
			.map(it -> {
				it.setApprovalNumber(reqParam.get("approvalNumber"));
				it.setDiagnosisNumber(reqParam.get("diagnosisNumber"));
				it.setProvince(reqParam.get("province"));
				it.setPersonalGroup(reqParam.get("personalGroup"));

				rezeptRepository.save(it);

				return "redirect:/rezepte/"+id;
			}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	/**
	 * Show a page with an instruction ("Bitte schließen Sie die offenen Rezepte für den Patient ab")
	 * and the prescriptions id
	 * @param patientId
	 * @return
	 */
	@GetMapping("/error/deletePatient/closePrescriptionMsg/{patientId}")
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	public String closePrescriptionMsg(@PathVariable String patientId, Model model){
		model.addAttribute("patient",
				patientServiceImpl.getPatientById(Long.valueOf(patientId)));
		model.addAttribute("prescriptions",
				rezeptManagement.findAllPrescriptionForPatientId(Long.valueOf(patientId))
						.stream().filter(rezept -> rezept.getStatus()==0).toList());
		return "closePrescriptionMsg";
	}

	@PreAuthorize("hasAnyAuthority('boss')")
	@GetMapping("/error/deletePersonal/closePrescriptionMsg/{personalId}")
	public String closePrescriptionMsgPersonal(@PathVariable String personalId, Model model){
		model.addAttribute("personal",
				personalServiceImpl.getPersonalById(Long.valueOf(personalId)));
		model.addAttribute("prescriptions",
				rezeptManagement.findAllPrescriptionForPersonalId(Long.valueOf(personalId))
						.stream().filter(rezept -> rezept.getStatus()==0).toList());
		return "closePrescriptionMsgPersonal";
	}
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	@GetMapping("/error/deleteLeistung/closePrescriptionMsg/{leistungId}")
	public String closePrescriptionMsgLeistung(@PathVariable String leistungId, Model model){
		model.addAttribute("leistung",
				leistungService.findLeistungById(Long.valueOf(leistungId)));
		model.addAttribute("prescriptions",
				rezeptManagement.findAllPrescriptionForLeistungId(Long.valueOf(leistungId))
						.stream().filter(rezept -> rezept.getStatus()==0).toList());
		return "closePrescriptionMsgLeistung";
	}
	/**
	 * Show a page with an instruction ("Sind Sie sicher, das Rezept mit erbrachten Leistungen zu löschen?")
	 * @param rezeptId, model
	 * @return
	 */
	@GetMapping("/error/deletePrescription/cannotDeletePrescriptionMsg/{rezeptId}")
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	public String cannotClosePrescriptionMsg(@PathVariable String rezeptId, Model model){
		model.addAttribute("prescription", rezeptManagement.findById(Long.valueOf(rezeptId)).orElseThrow());
		model.addAttribute("closedAppointments",
				rezeptManagement.findAllAppointmentsForPrescription(Long.valueOf(rezeptId))
						.stream().filter(termin -> termin.getStatus() == 1).toList());
		return "cannotDeletePrescriptionMsg";
	}

	/**
	 * Delete all the appointments and then their prescriptions
	 * all appointments should be completed
	 * Be called after a warning-page was served ("Are you sure to delete them...?")
	 * @param id Prescription ID
	 * @return The home page for Rezepterfassung
	 */
	@GetMapping("/rezepte/{id}/deleteClosedAppointments")
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	public String deleteCompletedAppointments(@PathVariable String id){
		for(Termin termin : rezeptManagement.findAllAppointmentsForPrescription(Long.valueOf(id))){
			terminplanerRepository.delete(termin);
		}
		rezeptManagement.delete(rezeptManagement.findById(Long.valueOf(id)).orElseThrow());
		return "redirect:/rezepte";
	}
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	@GetMapping("/error/updateLeistung/Msg/{leistungId}")
	public String cannotUpdateLeistungMsg(@PathVariable String leistungId, Model model){
		model.addAttribute("service",
				leistungService.findLeistungById(Long.valueOf(leistungId)));
		model.addAttribute("prescriptions",
				rezeptManagement.findAllPrescriptionForLeistungId(Long.valueOf(leistungId)));
		return "cannotUpdateLeistungMsg";
	}

	@GetMapping("/error/deletePersonal/closeAppointmentMsg/{therapistId}")
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	public String cannotDeleteTherapistMsg(@PathVariable String therapistId, Model model){
		model.addAttribute("therapist",
				personalServiceImpl.getPersonalById(Long.valueOf(therapistId)));
		model.addAttribute("appointments",
				rezeptManagement.findAllAppointmentsForPersonalId(Long.valueOf(therapistId)));
		return "closeAppointmentMsg";
	}

	/**
	 * Set the status of all AbrechnungEntry for the prescription as payment confirmed
	 * @param rezeptId
	 * @return Back to the same page
	 */
	@GetMapping("/rezepte/{rezeptId}/zahlung_bestaetigen")
	@PreAuthorize("hasAnyAuthority('boss','personal')")
	public String confirmPayment(@PathVariable String rezeptId, HttpSession session){
		long rezeptId_l = 0;
		try{
			rezeptId_l = Long.valueOf(rezeptId);
		}
		catch(NumberFormatException e){
			LOG.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Zahlungsbestätigung Fehlgeschlagen: Rezept mit ID " + rezeptId + " nicht gefunden");
		}
		for(AbrechnungEntry entry : abrechnungEntryRepository.findAll()){
			if(entry.getRezeptId() == rezeptId_l){
				entry.setConfirmed(true);
				abrechnungEntryRepository.save(entry);
			}
		}
		return "redirect:/rezepte/health_insurance/"+session.getAttribute("hiName");
	}
}
