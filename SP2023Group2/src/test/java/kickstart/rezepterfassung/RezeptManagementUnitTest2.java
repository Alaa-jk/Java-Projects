package kickstart.rezepterfassung;

import kickstart.leistungskatalog.Leistung;
import kickstart.leistungskatalog.LeistungRepository;
import kickstart.leistungskatalog.LeistungService;
import kickstart.patientenverwaltung.Patient;
import kickstart.patientenverwaltung.PatientRepository;
import kickstart.patientenverwaltung.PatientServiceImpl;
import kickstart.personalverwaltung.Personal;
import kickstart.personalverwaltung.PersonalRepository;
import kickstart.personalverwaltung.PersonalServiceImpl;
import kickstart.terminplaner.Termin;
import kickstart.terminplaner.TerminplanerRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class RezeptManagementUnitTest2 {
	@Autowired
	private TerminplanerRepository terminplanerRepository;
	@Autowired
	private RezeptManagement rezeptManagement;
	@Autowired
	private RezeptRepository rezeptRepository;
	@Autowired
	private PatientRepository patientRepository;
	@Autowired
	private PatientServiceImpl patientServiceImpl;
	@Autowired
	private PersonalRepository personalRepository;
	@Autowired
	private PersonalServiceImpl personalServiceImpl;
	@Autowired
	private LeistungRepository leistungRepository;
	@Autowired
	private LeistungService leistungService;
	@Autowired
	private LeistungInfoRepository leistungInfoRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeAll
	void setUp() {
		rezeptManagement.deleteAll();
		leistungRepository.deleteAll();
		patientRepository.deleteAll();
		personalRepository.deleteAll();
		List.of(
				new Leistung("Atemtherapie",300,"20"),
				new Leistung("Atemtherapie",400,"20"),
				new Leistung("Elektromassage",500,"20")
		).forEach(leistungRepository::save);
		List.of(
				new Patient("PAF1","PAL1", LocalDate.now(),RezeptController.healthInsurances[0],"",""),
				new Patient("PAF2","PAL2", LocalDate.now(),RezeptController.healthInsurances[1], "",""),
				new Patient("PAF3","PAL3", LocalDate.now(),RezeptController.healthInsurances[1], "","")
		).forEach(patientRepository::save);
		List.of(
				new Personal("boss","Freund", LocalDate.now(),
						null, null, null, "boss",
						passwordEncoder.encode("boss"), null, null, "boss"),
				new Personal("thera","Freund", LocalDate.now(),
						null, null, null, "thera",
						passwordEncoder.encode("password"), null, null, "therapist"),
				new Personal("rezeption","Freund", LocalDate.now(),
						null, null, null, "rezeption",
						passwordEncoder.encode("password"), null, null, "personal")
		).forEach(personalRepository::save);
	}

	@Test
	void findById() {
		rezeptRepository.deleteAll();
		assertThat(rezeptManagement.findById(0)).isEmpty();
		Leistung leistung = leistungService.getAllLeistungen().get(0);
		Leistung leistung2 = leistungService.getAllLeistungen().get(1);
		Patient patient = patientRepository.findAll().stream().findFirst().orElseThrow();
		Rezept rezept = new Rezept(
				patient, personalRepository.findAll().iterator().next(),
				List.of(new LeistungInfo(leistung, 5,0,0),
						new LeistungInfo(leistung2, 5,0,0)),
				0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptManagement.save(rezept);
		assertThat(rezeptManagement.findById(rezept.getId())
				.orElseThrow().getProvince()).isEqualTo("Bayern");
		rezeptRepository.deleteAll();
	}

	@Test
	void findAll() {
		assertThat(List.of(rezeptRepository.findAll()).stream().count())
				.isEqualTo(List.of(rezeptManagement.findAll()).stream().count());
	}

	@Test
	void save() {
		terminplanerRepository.deleteAll();
		rezeptRepository.deleteAll();
		long countBefore = rezeptRepository.count();
		rezeptRepository.save(new Rezept());
		assertThat(rezeptRepository.count()-countBefore).isEqualTo(1);
		rezeptRepository.deleteAll();
	}

	@Test
	void findByHealthInsurance() {
		Rezept rezept = new Rezept(patientServiceImpl.getAllPatients().get(0),
				personalRepository.findAll().iterator().next(),
				List.of(new LeistungInfo(leistungService.getAllLeistungen().get(0), 5,0,0),
						new LeistungInfo(leistungService.getAllLeistungen().get(1), 5,0,0)),
				0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptManagement.save(rezept);
		rezeptManagement.findByHealthInsurance(RezeptController.healthInsurances[0]).forEach(r -> {
			assertThat(r.getHealthInsurance()).isEqualTo(RezeptController.healthInsurances[0]);
		});
		rezeptManagement.deleteAll();
	}

	@Test
	void delete() {
		Rezept rezept = new Rezept(patientServiceImpl.getAllPatients().get(0),
				personalRepository.findAll().iterator().next(),
				List.of(new LeistungInfo(leistungService.getAllLeistungen().get(0), 5,0,0),
						new LeistungInfo(leistungService.getAllLeistungen().get(1), 5,0,0)),
				0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptManagement.save(rezept);
		long countBefore = rezeptRepository.count();
		rezeptManagement.delete(rezept);
		assertThat(countBefore - rezeptRepository.count()).isEqualTo(1);
	}

	@Test
	void deleteAll() {
		rezeptManagement.deleteAll();
		assertThat(rezeptRepository.count()).isEqualTo(0);
	}

	@Test
	void getLeistungInfoListById() {
		Rezept rezept = new Rezept(patientServiceImpl.getAllPatients().get(0),
				personalRepository.findAll().iterator().next(),
				List.of(new LeistungInfo(leistungService.getAllLeistungen().get(0), 5,0,0),
						new LeistungInfo(leistungService.getAllLeistungen().get(1), 5,0,0)),
				0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptManagement.save(rezept);
		assertThat(rezeptManagement.getLeistungInfoListById(rezept.getId()).get(0).getLeistung().getName())
				.isEqualTo("Atemtherapie");
		assertThat(rezeptManagement.getLeistungInfoListById(rezept.getId()).size()).isEqualTo(2);
		rezeptManagement.deleteAll();
	}

	@Test
	@Transactional
	void addLeistungInfoToList() {
		Leistung le1 = leistungService.getAllLeistungen().get(0);
		Leistung le2 = leistungService.getAllLeistungen().get(1);
		LeistungInfo li1 = new LeistungInfo(le1, 5,0,0);
		leistungInfoRepository.save(li1);
		Rezept rezept = new Rezept(patientServiceImpl.getAllPatients().get(0),
				personalRepository.findAll().iterator().next(),
				new ArrayList<LeistungInfo>(List.of(li1)),
				0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptManagement.save(rezept);
		long countBefore = 0;
		for(LeistungInfo li : rezeptRepository.findById(rezept.getId()).orElseThrow()
				.getLeistungInfoList()){
			countBefore++;
		}
		rezeptManagement.addLeistungInfoToList(rezept.getId(), le1.getId(), 3);
		Rezept rezeptAfter = rezeptRepository.findById(rezept.getId()).orElseThrow();
		assertThat(rezeptAfter.getLeistungInfoList().size() - countBefore).isEqualTo(0);
		rezeptManagement.addLeistungInfoToList(rezept.getId(), le2.getId(), 2);
		assertThat(rezeptAfter.getLeistungInfoList().size() - countBefore).isEqualTo(1);
	}

	@Test
	@Transactional
	void existsPrescriptionForPatient() {
		Patient p1 = patientRepository.findAll().stream().findFirst().orElseThrow();
		//Check for an any patient with a prescription
		Rezept rezept = new Rezept(
				p1, personalRepository.findAll().iterator().next(),
				new LinkedList<LeistungInfo>(), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptRepository.save(rezept);
		assertThat(rezeptManagement.existsPrescriptionForPatient(p1.getId())).isTrue();
		//Check for a patient with no prescription
		Patient p2 = new Patient("PAF4","PAL4", LocalDate.now(),"","","");
		patientServiceImpl.savePatient(p2);
		assertThat(rezeptManagement.existsPrescriptionForPatient(p2.getId())).isFalse();
	}

	@Test
	void existsOpenPrescriptionForPatientId() {
		Patient p1 = patientRepository.findAll().stream().findFirst().orElseThrow();
		Rezept rezept = new Rezept(
				p1, personalRepository.findAll().iterator().next(),
				new LinkedList<LeistungInfo>(), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptRepository.save(rezept);
		assertThat(rezeptManagement.existsOpenPrescriptionForPatientId(p1.getId())).isTrue();
		Patient p2 = patientRepository.findAll().stream().findFirst().orElseThrow();
		Rezept rezept2 = new Rezept(
				p2, personalRepository.findAll().iterator().next(),
				new LinkedList<LeistungInfo>(), 1, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptRepository.save(rezept2);
		assertThat(rezeptManagement.existsOpenPrescriptionForPatientId(p2.getId())).isTrue();
	}

	@Test
	void findAllPrescriptionForPatientId() {
		Patient p1 = patientRepository.findAll().stream().findFirst().orElseThrow();
		Rezept rezept = new Rezept(
				p1, personalRepository.findAll().iterator().next(),
				new LinkedList<LeistungInfo>(), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		Rezept rezept2 = new Rezept(
				p1, personalRepository.findAll().iterator().next(),
				new LinkedList<LeistungInfo>(), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptRepository.save(rezept);
		rezeptRepository.save(rezept2);
		assertThat(rezeptManagement.findAllPrescriptionForPatientId(p1.getId()).stream().count()).isEqualTo(2);
	}

	@Test
	void existsAppointmentForPatientId(){
		Leistung leistung = leistungService.getAllLeistungen().get(0);
		Patient patient = patientRepository.findAll().stream().findFirst().orElseThrow();
		Rezept rezept = new Rezept(
				patient, personalRepository.findAll().iterator().next(),
				List.of(new LeistungInfo(leistung, 5,0,0)), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptManagement.save(rezept);
		terminplanerRepository.save(new Termin(
				patient, personalRepository.findAll().iterator().next(),
				rezept, leistung, 0, 0, 0, LocalDate.now()
		));
		assertThat(rezeptManagement.existsAppointmentForPatientId(patient.getId())).isTrue();
		Patient p2 = new Patient("PAF4","PAL4", LocalDate.now(),"","","");
		patientServiceImpl.savePatient(p2);
		assertThat(rezeptManagement.existsAppointmentForPatientId(p2.getId())).isFalse();
	}

	@Test
	void deleteAllPrescriptionsForPatientId(){
		Leistung leistung = leistungService.getAllLeistungen().get(0);
		Patient patient = patientRepository.findAll().stream().findFirst().orElseThrow();
		Rezept rezept = new Rezept(
				patient, personalRepository.findAll().iterator().next(),
				List.of(new LeistungInfo(leistung, 5,0,0)), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptManagement.save(rezept);
		long countBefore = rezeptRepository.count();
		rezeptManagement.deleteAllPrescriptionsForPatientId(patient.getId());
		assertThat(countBefore-rezeptRepository.count()).isEqualTo(1);
		assertThat(rezeptManagement.existsPrescriptionForPatient(patient.getId())).isFalse();
	}

	@Test
	void existsClosedAppointmentForPatientId(){
		Leistung leistung = leistungService.getAllLeistungen().get(0);
		Patient patient = patientRepository.findAll().stream().findFirst().orElseThrow();
		Rezept rezept = new Rezept(
				patient, personalRepository.findAll().iterator().next(),
				List.of(new LeistungInfo(leistung, 5,0,0)), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptManagement.save(rezept);
		Termin termin = new Termin(
				patient, personalRepository.findAll().iterator().next(),
				rezept, leistung, 1, 0, 0, LocalDate.now()
		);
		terminplanerRepository.save(termin);
		assertThat(rezeptManagement.existsClosedAppointmentForPatientId(patient.getId()))
				.isTrue();
		termin.setStatus(0);
		terminplanerRepository.save(termin);
		assertThat(rezeptManagement.existsClosedAppointmentForPatientId(patient.getId()))
				.isFalse();
	}

	@Test
	void deleteAllAppointmentsForPatientId(){
		terminplanerRepository.deleteAll();
		Leistung leistung = leistungService.getAllLeistungen().get(0);
		Leistung leistung2 = leistungService.getAllLeistungen().get(1);
		Patient patient = patientRepository.findAll().stream().findFirst().orElseThrow();
		Rezept rezept = new Rezept(
				patient, personalRepository.findAll().iterator().next(),
				List.of(new LeistungInfo(leistung, 5,0,0),
						new LeistungInfo(leistung2, 5,0,0)),
				0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptManagement.save(rezept);
		Termin termin = new Termin(
				patient, personalRepository.findAll().iterator().next(),
				rezept, leistung, 1, 0, 1, LocalDate.now()
		);
		Termin termin2 = new Termin(
				patient, personalRepository.findAll().iterator().next(),
				rezept, leistung2, 1, 1, 1, LocalDate.now()
		);
		terminplanerRepository.save(termin);
		terminplanerRepository.save(termin2);
		long countBefore = terminplanerRepository.count();
		rezeptManagement.deleteAllAppointmentsForPatientId(patient.getId());
		assertThat(countBefore - terminplanerRepository.count()).isEqualTo(2);
		assertThat(rezeptManagement.existsAppointmentForPatientId(patient.getId())).isFalse();
		terminplanerRepository.deleteAll();
		rezeptRepository.deleteAll();
	}

	@Test
	void deletePatient() {
		Leistung leistung = leistungService.getAllLeistungen().get(0);
		Patient patient1 = patientRepository.findAll().stream().toList().get(0);
		Patient patient2 = patientRepository.findAll().stream().toList().get(1);
 		Rezept rezept = new Rezept(
				patient1, personalRepository.findAll().iterator().next(),
				List.of(new LeistungInfo(leistung, 5,0,0)), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptManagement.save(rezept);
		//if there are no prescription for the patient
		assertThat(rezeptManagement.deletePatient(patient2.getId())).isEqualTo("redirect:/patient");
		//if exits
		assertThat(rezeptManagement.deletePatient(patient1.getId()))
				.isEqualTo("redirect:/error/deletePatient/closePrescriptionMsg/"+patient1.getId());
	}

	@Test
	void existsOpenAppointmentForPrescription() {
		Leistung leistung = leistungService.getAllLeistungen().get(0);
		Patient patient1 = patientRepository.findAll().stream().toList().get(0);
		Personal personal = personalRepository.findAll().iterator().next();
		Rezept rezept1 = new Rezept(
				patient1, personalRepository.findAll().iterator().next(),
				List.of(new LeistungInfo(leistung, 5,0,0)),
				0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		Rezept rezept2 = new Rezept(
				patient1, personalRepository.findAll().iterator().next(),
				List.of(new LeistungInfo(leistung, 5,0,0)),
				0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptManagement.save(rezept1);
		rezeptManagement.save(rezept2);
		Termin termin = new Termin(
				patient1, personal, rezept1,leistung,0,0,5,LocalDate.now());
		terminplanerRepository.save(termin);
		//It exists for rezept1 an open appointment, not for rezept2
		assertThat(rezeptManagement.existsOpenAppointmentForPrescription(rezept1.getId()))
				.isTrue();
		assertThat(rezeptManagement.existsOpenAppointmentForPrescription(rezept2.getId()))
				.isFalse();
		terminplanerRepository.deleteAll();
		rezeptManagement.deleteAll();
	}
}
