package kickstart.rezepterfassung;

import kickstart.leistungskatalog.Leistung;
import kickstart.leistungskatalog.LeistungService;
import kickstart.patientenverwaltung.*;
import kickstart.personalverwaltung.Personal;
import kickstart.personalverwaltung.PersonalService;
import kickstart.terminplaner.Termin;
import kickstart.terminplaner.TerminService;
import kickstart.terminplaner.TerminplanerRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Transactional
public class RezeptManagement{
	private final TerminplanerRepository terminplanerRepository;
	private final PatientServiceImpl patientServiceImpl;
	private final PatientService patientService;
	private final PersonalService personalService;
	private final LeistungService leistungService;
	private final LeistungInfoRepository leistungInfoRepository;
	private final RezeptRepository rezeptRepository;
	private final AbrechnungEntryRepository abrechnungEntryRepository;
	private final TerminService terminService;

	RezeptManagement(TerminplanerRepository terminplanerRepository,
					 LeistungService leistungService,
					 LeistungInfoRepository leistungInfoRepository,
					 RezeptRepository rezeptRepository,
					 PatientService patientService,
					 PersonalService personalService,
					 PatientServiceImpl patientServiceImpl,
					 AbrechnungEntryRepository abrechnungEntryRepository,
					 TerminService terminService) {
		this.terminplanerRepository = terminplanerRepository;
		this.leistungService = leistungService;
		this.leistungInfoRepository = leistungInfoRepository;
		this.rezeptRepository = rezeptRepository;
		this.patientServiceImpl = patientServiceImpl;
		this.patientService = patientService;
		this.abrechnungEntryRepository = abrechnungEntryRepository;
		this.personalService = personalService;
		this.terminService = terminService;
		Assert.notNull(leistungService, "LeistungService must not be null!");
		Assert.notNull(leistungInfoRepository, "LeistungInfoRepository must not be null!");
		Assert.notNull(rezeptRepository, "RezeptRepository must not be null!");
		Assert.notNull(patientService, "PatientService must not be null!");
		Assert.notNull(personalService, "PersonalService must not be null!");
	}
	public Optional<Rezept> findById(long id){
		return rezeptRepository.findById(id);
	}

	public Iterable<Rezept> findAll() {
		return rezeptRepository.findAll();
	}

	public void save(Rezept rezept) {
		ArrayList<LeistungInfo> newList = new ArrayList<>(rezept.getLeistungInfoList());
		rezept.setLeistungInfoList(newList);
		rezeptRepository.save(rezept);
	}

	public Iterable<Rezept> findByHealthInsurance(String hiName) {

		return rezeptRepository.findByHealthInsurance(hiName);
	}

	public void delete(Rezept it) {
		rezeptRepository.delete(it);
	}

	public void deleteAll() {
		rezeptRepository.deleteAll();
	}

	public List<LeistungInfo> getLeistungInfoListById(long rezeptId) {
		return findById(rezeptId).map(rezept -> {
			return Streamable.of(rezept.getLeistungInfoList()).toList();
		}).orElseThrow();
	}

	public void addLeistungInfoToList(long rezeptId, long serviceId, int countService) {
		Leistung leistung = leistungService.findLeistungById(serviceId);
		Rezept rezept = findById(rezeptId).orElseThrow();
		AtomicBoolean contains = new AtomicBoolean(false);
		rezept.getLeistungInfoList().forEach(leistungInfo -> {
			if(leistungInfo.getLeistung().getId() == serviceId) {
				contains.set(true);
				leistungInfo.setMaxCount(
						leistungInfo.getMaxCount()+countService);
				leistungInfoRepository.save(leistungInfo);
				save(rezept);
			}
		});
		if(!contains.get()){
			LeistungInfo leistungInfo
					= new LeistungInfo(leistung, countService,0,0);

			leistungInfoRepository.save(leistungInfo);
			rezept.getLeistungInfoList().add(leistungInfo);
			save(rezept);
		}
	}

	/**
	 * Check if any prescription exists for the patient
	 * @param patientId
	 * @return
	 */
	public boolean existsPrescriptionForPatient(Long patientId){
		for(Rezept rezept : findAll()){
			if(rezept.getPatient().getId() == patientId)
				return true;
		}
		return false;
	}

	public boolean existsOpenPrescriptionForPatientId(Long patientId){
		for(Rezept rezept : findAllPrescriptionForPatientId(patientId)){
			if(rezept.getStatus() == 0)
				return true;
		}
		return false;
	}

	public List<Rezept> findAllPrescriptionForPatientId(Long patientId){
		List<Rezept> result = new LinkedList<>();
		for(Rezept rezept : findAll()){
			if(rezept.getPatient().getId() == patientId)
				result.add(rezept);
		}
		return result;
	}

	public boolean existsAppointmentForPatientId(Long patientId){
		for(Termin termin : terminplanerRepository.findAll()){
			if(termin.getPatient().getId() == patientId)
				return true;
		}
		return false;
	}

	/**
	 * May be called only if there is no appointment for the prescriptions
	 * @param patientId
	 */
	public void deleteAllPrescriptionsForPatientId(long patientId){
		findAllPrescriptionForPatientId(patientId).forEach(this::delete);
	}

	public boolean existsClosedAppointmentForPatientId(long patientId){
		for(Termin termin : terminplanerRepository.findAll()){
			if(termin.getPatient().getId() == patientId && termin.getStatus() == 1)
				return true;
		}
		return false;
	}

	public void deleteAllAppointmentsForPatientId(long patientId){
		for(Termin termin : terminplanerRepository.findAll()){
			if(termin.getPatient().getId() == patientId) {
				terminplanerRepository.delete(termin);
			}
		}
	}

	public boolean existsOpenAppointmentForPrescription(long rezeptId){
		Rezept rezept = findById(rezeptId).orElseThrow();
		for(Termin termin : terminplanerRepository.findAll()){
			if(termin.getRezept().getId() == rezept.getId()
					&& termin.getStatus() == 0){
				return true;
			}
		}
		return false;
	}
	public List<Termin> findAllAppointmentsForPrescription(long rezeptId){
		List<Termin> result = new LinkedList<>();
		for(Termin termin : terminplanerRepository.findAll()){
			if(termin.getRezept().getId() == rezeptId) {
				result.add(termin);
			}
		}
		return result;
	}

	public boolean existsAppointmentForPrescription(Rezept rezept){
		for(Termin termin : terminplanerRepository.findAll()){
			if(termin.getRezept().getId() == rezept.getId())
				return true;
		}
		return false;
	}

	public boolean existsClosedAppointmentForPrescription(Rezept rezept){
		for(Termin termin : terminplanerRepository.findAll()){
			if(termin.getRezept().getId() == rezept.getId()
					&& termin.getStatus() == 1)
				return true;
		}
		return false;
	}

	/**
	 * Archive closed prescriptions for the patient who should be removed
	 * @param patientId
	 */
		public void archivePrescriptionForPatient(long patientId){

		for(AbrechnungEntry a : abrechnungEntryRepository.findAll()){
			if(a.getPatientId() == patientId){
				a.setArchived(true);
				abrechnungEntryRepository.save(a);
			}
		}
	}

	/**
	 * Check if a patient can be deleted
	 *
	 * @param patientId
	 * @return
	 */
    public String deletePatient(Long patientId) {
		//Check if the patient has a Rezept
		if(!existsPrescriptionForPatient(patientId)) {//If not then delete him
			patientService.deletePatientById(patientId);
			return "redirect:/patient";
		}
		//Check if any prescription exists which hasn't been closed yet
		if(existsOpenPrescriptionForPatientId(patientId)){
			//If exists then it should be closed manually
			return "redirect:/error/deletePatient/closePrescriptionMsg/"+patientId;
		}
		archivePrescriptionForPatient(patientId);

		//Delete the old appointments and then the old prescriptions
		deleteAllAppointmentsForPatientId(patientId);
		deleteAllPrescriptionsForPatientId(patientId);
		patientService.deletePatientById(patientId);
		return "redirect:/patient";
	}

	/**
	 * Check if a prescription can be closed
	 * @param rezeptId
	 * @return
	 */
	public String closePrescription(long rezeptId) {
		Rezept rezept = findById(rezeptId).orElseThrow();
		//Check if there is any open appointment
		if(existsOpenAppointmentForPrescription(rezeptId)){
			//Delete open appointments and then mark the prescription as closed
			for(Termin termin : findAllAppointmentsForPrescription(rezeptId)){
				if(termin.getStatus() == 0){
					terminplanerRepository.delete(termin);
				}
			}
		}

		convertRezeptToAbrechnungEntry(rezeptId, false);

		rezept.setStatus(1);
		rezeptRepository.save(rezept);

		return "redirect:/rezepte";
	}

	/**
	 * Save AbrechnungEntry as a copy of the Rezept
	 * @param rezeptId
	 * @param archived
	 */
	public void convertRezeptToAbrechnungEntry(long rezeptId, boolean archived){
		Rezept rezept = rezeptRepository.findById(rezeptId)
				.orElseThrow();
		rezept.getLeistungInfoList().forEach(leistungInfo -> {
			abrechnungEntryRepository.save(new AbrechnungEntry(
					rezept.getPatient().getFirstname(),
					rezept.getPatient().getLastname(),
					rezept.getPatient().getBirthdate(),
					rezept.getPatient().getId(),
					rezept.getPersonal().getFirstname(),
					rezept.getPersonal().getLastname(),
					rezept.getPersonal().getBirthdate(),
					rezept.getPersonal().getId(),
					rezept.getId(),
					leistungInfo.getLeistung().getId(),
					leistungInfo.getLeistung().getName(),
					leistungInfo.getPriceInCent(),
					leistungInfo.getActCount(),
					leistungInfo.getActCount() > 0,
					rezept.getApprovalNumber(),
					rezept.getDiagnosisNumber(),
					rezept.getProvince(),
					rezept.getPersonalGroup(),
					rezept.getHealthInsurance(),
					archived
			));
		});
	}

	/**
	 * Check if there is any appointment for the prescription by deleting it
	 * @param rezept
	 * @return redirect URL
	 */
	public String deletePrescription(Rezept rezept){
		if(!existsAppointmentForPrescription(rezept)){
			//It's safe to delete
			rezeptRepository.delete(rezept);
			return "redirect:/rezepte";
		}
		if(existsClosedAppointmentForPrescription(rezept)){
			//The prescription cannot be deleted and its appointments should be manually corrected
			return "redirect:/error/deletePrescription/cannotDeletePrescriptionMsg/"+rezept.getId();
		}
		//There are only open appointments, so cancel all of them before deleting the prescription
		for(Termin termin : findAllAppointmentsForPrescription(rezept.getId())){
			terminplanerRepository.delete(termin);
		}
		rezeptRepository.delete(rezept);
		return "redirect:/rezepte";
	}

    public String deletePersonal(Long personalId) {
		//Check if the personal has written a prescription
		if(!existsPrescriptionForPersonal(personalId)) {
			//Check if the personal is responsible for an appointment (as a therapist)
			if(!existsAppointmentForPersonalId(personalId)) {
				personalService.deletePersonalById(personalId);
				return "redirect:/personal";
			}
			else {
				//Check if there is any appointment of him which is open
				if(existsOpenAppointmentForPersonal(personalId)){
					//If exists then it should be closed manually
					return "redirect:/error/deletePersonal/closeAppointmentMsg/"+personalId;
				}
				else {//If all of his appointments have been closed (Leistungen wurden erbracht)
					//then they can be deleted (prescriptions will exist further)
					deleteAllAppointmentsForPersonalId(personalId);
					personalService.deletePersonalById(personalId);
					return "redirect:/personal";
				}
			}
		}
		//Check if any prescription exists which hasn't been closed yet
		if(existsOpenPrescriptionForPersonalId(personalId)){
			//If exists then it should be closed manually
			return "redirect:/error/deletePersonal/closePrescriptionMsg/"+personalId;
		}
		archivePrescriptionForPersonal(personalId);

		//Delete the old appointments and then the old prescriptions
		deleteAllAppointmentsForPersonalId(personalId);
		deleteAllPrescriptionsForPersonalId(personalId);
		personalService.deletePersonalById(personalId);
		return "redirect:/personal";
    }

	public boolean existsOpenAppointmentForPersonal(Long personalId) {
		Personal therapist = personalService.getPersonalById(personalId);
		for(Termin termin : terminplanerRepository.findAll()){
			if(termin.getTherapist().getId() == personalId
					&& termin.getStatus() == 0){
				return true;
			}
		}
		return false;
	}

	public boolean existsAppointmentForPersonalId(Long personalId) {
		for(Termin termin : terminplanerRepository.findAll()){
			if(termin.getTherapist().getId() == personalId)
				return true;
		}
		return false;
	}

	public void archivePrescriptionForPersonal(Long personalId) {
		for(AbrechnungEntry a : abrechnungEntryRepository.findAll()){
			if(personalId == a.getPersonalId()){
				a.setArchived(true);
				abrechnungEntryRepository.save(a);
			}
		}
	}

	public void deleteAllPrescriptionsForPersonalId(Long personalId) {
		findAllPrescriptionForPersonalId(personalId).forEach(this::delete);
	}

	public void deleteAllAppointmentsForPersonalId(Long personalId) {
		for(Termin termin : terminplanerRepository.findAll()){
			if(termin.getTherapist().getId() == personalId
			|| termin.getRezept().getPersonal().getId() == personalId) {
				terminplanerRepository.delete(termin);
			}
		}
	}

	public boolean existsOpenPrescriptionForPersonalId(Long personalId) {
		for(Rezept rezept : findAllPrescriptionForPersonalId(personalId)){
			if(rezept.getStatus() == 0)
				return true;
		}
		return false;
	}

	public List<Rezept> findAllPrescriptionForPersonalId(Long personalId) {
		List<Rezept> result = new LinkedList<>();
		for(Rezept rezept : findAll()){
			if(rezept.getPersonal().getId() == personalId)
				result.add(rezept);
		}
		return result;
	}

	private boolean existsPrescriptionForPersonal(Long personalId) {
		for(Rezept rezept : findAll()){
			if(rezept.getPersonal().getId() == personalId)
				return true;
		}
		return false;
	}

	public String deleteLeistung(Long leistungId) {
		//Check if there is a prescription for this service
		if(!existsPrescriptionForLeistung(leistungId)) {//If not then delete him
			leistungService.deleteLeistungById(leistungId);
			return "redirect:/Katalogleistungen";
		}
		//Check if any prescription exists which hasn't been closed yet
		if(existsOpenPrescriptionForLeistungId(leistungId)){
			//If exists then it should be closed manually
			return "redirect:/error/deleteLeistung/closePrescriptionMsg/"+leistungId;
		}
		archivePrescriptionForLeistung(leistungId);

		//Delete the old appointments and then the old prescriptions
		deleteAllAppointmentsForLeistungId(leistungId);
		deleteAllPrescriptionsForLeistungId(leistungId);
		leistungService.deleteLeistungById(leistungId);
		return "redirect:/Katalogleistungen";
	}

	private void deleteAllPrescriptionsForLeistungId(Long leistungId) {
		findAllPrescriptionForLeistungId(leistungId).forEach(this::delete);
	}

	public List<Rezept> findAllPrescriptionForLeistungId(Long leistungId) {
		List<Rezept> result = new LinkedList<>();
		for(Rezept rezept : findAll()){
			for(LeistungInfo leistungInfo : rezept.getLeistungInfoList()){
				if(leistungInfo.getLeistung().getId() == leistungId){
					result.add(rezept);
					break;
				}
			}
		}
		return result;
	}

	private void deleteAllAppointmentsForLeistungId(Long leistungId) {
		for(Rezept rezept : findAllPrescriptionForLeistungId(leistungId)){
			for(Termin termin : terminplanerRepository.findAll()){
				if(termin.getRezept().getId() == rezept.getId()) {
					terminplanerRepository.delete(termin);
				}
			}
		}
	}

	private void archivePrescriptionForLeistung(Long leistungId) {
		for(AbrechnungEntry a : abrechnungEntryRepository.findAll()){
			if(a.getLeistungId() == leistungId){
				a.setArchived(true);
				abrechnungEntryRepository.save(a);
			}
		}
	}

	private boolean existsOpenPrescriptionForLeistungId(Long leistungId) {
		for(Rezept rezept : findAllPrescriptionForLeistungId(leistungId)){
			if(rezept.getStatus() == 0)
				return true;
		}
		return false;
	}

	public boolean existsPrescriptionForLeistung(Long leistungId) {
		for(Rezept rezept : findAll()){
			for(LeistungInfo leistungInfo : rezept.getLeistungInfoList()){
				if(leistungInfo.getLeistung().getId()==leistungId)
					return true;
			}
		}
		return false;
	}

	public List<Termin> findAllAppointmentsForPersonalId(Long therapistId) {
		List<Termin> result = new LinkedList<>();
		for(Termin termin : terminplanerRepository.findAll()){
			if(termin.getTherapist().getId() == therapistId) {
				result.add(termin);
			}
		}
		return result;
	}
}
