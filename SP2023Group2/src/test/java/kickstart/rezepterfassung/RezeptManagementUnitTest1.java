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

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class RezeptManagementUnitTest1 {
	@Autowired
	private AbrechnungEntryRepository abrechnungEntryRepository;
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
						passwordEncoder.encode("password"), null, null, "personal"),
				new Personal("thea","Josef", LocalDate.now(),
						null, null, null, "thea",
						passwordEncoder.encode("password"), null, null, "personal")
		).forEach(personalRepository::save);
	}

	@Test
	void findAllAppointmentsForPrescription() {
		Leistung le = leistungRepository.getAllLeistungen().get(0);
		Personal pe = personalRepository.getAllPersonal().get(0);
		Patient p1 = patientRepository.findAll().stream().findFirst().orElseThrow();
		Rezept rezept = new Rezept(
				p1, personalRepository.findAll().iterator().next(),
				new LinkedList<LeistungInfo>(), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptRepository.save(rezept);

		Termin t1 = new Termin(p1, pe, rezept, le, 0,0,1,LocalDate.now());
		Termin t2 = new Termin(p1, pe, rezept, le, 0,1,1,LocalDate.now());
		List.of(t1,t2).forEach(terminplanerRepository::save);
		assertThat(rezeptManagement.findAllAppointmentsForPrescription(rezept.getId()).size())
				.isEqualTo(2);
		terminplanerRepository.deleteAll();
		rezeptRepository.deleteAll();
	}

	@Test
	void existsAppointmentForPrescription() {
		Leistung le = leistungRepository.getAllLeistungen().get(0);
		Personal pe = personalRepository.getAllPersonal().get(0);
		Patient p1 = patientRepository.findAll().stream().findFirst().orElseThrow();
		Rezept rezept = new Rezept(
				p1, personalRepository.findAll().iterator().next(),
				new LinkedList<LeistungInfo>(), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptRepository.save(rezept);
		Rezept rezept2 = new Rezept(
				p1, personalRepository.findAll().iterator().next(),
				new LinkedList<LeistungInfo>(), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptRepository.save(rezept2);
		Termin t1 = new Termin(p1, pe, rezept, le, 0,0,1,LocalDate.now());
		terminplanerRepository.save(t1);
		assertThat(rezeptManagement.existsAppointmentForPrescription(rezept)).isTrue();
		assertThat(rezeptManagement.existsAppointmentForPrescription(rezept2)).isFalse();
		terminplanerRepository.deleteAll();
		rezeptRepository.deleteAll();
	}

	@Test
	void existsClosedAppointmentForPrescription() {
		Leistung le = leistungRepository.getAllLeistungen().get(0);
		Personal pe = personalRepository.getAllPersonal().get(0);
		Patient p1 = patientRepository.findAll().stream().findFirst().orElseThrow();
		Rezept rezept = new Rezept(
				p1, personalRepository.findAll().iterator().next(),
				new LinkedList<LeistungInfo>(), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptRepository.save(rezept);
		Rezept rezept2 = new Rezept(
				p1, personalRepository.findAll().iterator().next(),
				new LinkedList<LeistungInfo>(), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		List.of(rezept, rezept2).forEach(rezeptRepository::save);
		Termin t1 = new Termin(p1, pe, rezept, le, 0,0,1,LocalDate.now());
		Termin t2 = new Termin(p1, pe, rezept2, le, 1,1,1,LocalDate.now());
		List.of(t1,t2).forEach(terminplanerRepository::save);
		assertThat(rezeptManagement.existsOpenAppointmentForPrescription(rezept.getId())).isTrue();
		assertThat(rezeptManagement.existsOpenAppointmentForPrescription(rezept2.getId())).isFalse();
		terminplanerRepository.deleteAll();
		rezeptRepository.deleteAll();
	}

	@Test
	@Transactional
	void archivePrescriptionForPatient() {
		Leistung le = leistungRepository.getAllLeistungen().get(0);
		Personal pe = personalRepository.getAllPersonal().get(0);
		Patient p1 = patientRepository.findAll().stream().findFirst().orElseThrow();
		Rezept rezept = new Rezept(
				p1, personalRepository.findAll().iterator().next(),
				new LinkedList<LeistungInfo>(), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptRepository.save(rezept);
		rezeptManagement.archivePrescriptionForPatient(p1.getId());
		List<LeistungInfo> lst = new LinkedList<>();
		rezeptManagement.findAll().forEach(r -> {
			if(p1.getId() == r.getPatient().getId()){
				lst.addAll(r.getLeistungInfoList());
			}
		});
		for(LeistungInfo leistungInfo : lst){
			abrechnungEntryRepository
					.findByLeistungId(leistungInfo.getLeistung().getId())
					.forEach(abrechnungEntry -> {
						assertThat(abrechnungEntry.isArchived()).isTrue();
					});
		}
		rezeptManagement.deleteAll();
	}

	@Test
	void closePrescription() {
		Leistung le = leistungRepository.getAllLeistungen().get(0);
		Personal pe = personalRepository.getAllPersonal().get(0);
		Patient p1 = patientRepository.findAll().stream().findFirst().orElseThrow();
		Rezept rezept = new Rezept(
				p1, personalRepository.findAll().iterator().next(),
				new LinkedList<LeistungInfo>(), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptRepository.save(rezept);
		rezeptManagement.closePrescription(rezept.getId());
		assertThat(rezeptManagement.findById(rezept.getId())
				.orElseThrow().getStatus()).isEqualTo(1);
		rezeptManagement.deleteAll();
	}

	@Test
	void convertRezeptToAbrechnungEntry() {
		abrechnungEntryRepository.deleteAll();
		Leistung le = leistungRepository.getAllLeistungen().get(0);
		Personal pe = personalRepository.getAllPersonal().get(0);
		Patient p1 = patientRepository.findAll().stream().findFirst().orElseThrow();
		LinkedList<LeistungInfo> list = new LinkedList<LeistungInfo>();
		LeistungInfo l1 = new LeistungInfo(le, 1,0,3);
		LeistungInfo l2 = new LeistungInfo(le, 2,0,3);
		list.add(l1);
		list.add(l2);
		Rezept rezept = new Rezept(
				p1, personalRepository.findAll().iterator().next(),
				list, 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptRepository.save(rezept);
		long countBefore = abrechnungEntryRepository.count();
		rezeptManagement.convertRezeptToAbrechnungEntry(rezept.getId(), false);
		//Should be increased by the count of LeistungInfo in list
		assertThat(abrechnungEntryRepository.count() - countBefore).isEqualTo(2);
		abrechnungEntryRepository.findAll().forEach(a -> {
			assertThat(a.getLeistungCount()).isEqualTo(3);
			assertThat(a.getHealthInsurance()).isEqualTo(p1.getHealthInsurance());
		});
		abrechnungEntryRepository.deleteAll();
		rezeptManagement.deleteAll();
	}

	@Test
	void deletePrescription() {
		Leistung le = leistungRepository.getAllLeistungen().get(0);
		Personal pe = personalRepository.getAllPersonal().get(0);
		Patient p1 = patientRepository.findAll().stream().findFirst().orElseThrow();
		LeistungInfo l1 = new LeistungInfo(le, 1,0,0);
		LeistungInfo l2 = new LeistungInfo(le, 2,0,0);
		LeistungInfo l3 = new LeistungInfo(le, 2,0,0);
		Rezept rezept = new Rezept(
				p1, personalRepository.findAll().iterator().next(),
				List.of(l1), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		Rezept rezept2 = new Rezept(
				p1, personalRepository.findAll().iterator().next(),
				List.of(l2), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		Rezept rezept3 = new Rezept(
				p1, personalRepository.findAll().iterator().next(),
				List.of(l3), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		List.of(rezept, rezept2, rezept3).forEach(rezeptRepository::save);
		Termin t3 = new Termin(p1,pe,rezept3,le,0,1,1,LocalDate.now());
		terminplanerRepository.save(new Termin(p1,pe,rezept2,le,1,0,1,LocalDate.now()));
		terminplanerRepository.save(t3);
		//If there is no appointment for the prescription
		assertThat(rezeptManagement.deletePrescription(rezept)).isEqualTo("redirect:/rezepte");
		//If there is any closed appointment
		assertThat(rezeptManagement.deletePrescription(rezept2))
				.isEqualTo("redirect:/error/deletePrescription/cannotDeletePrescriptionMsg/"+rezept2.getId());
		assertThat(rezeptRepository.findById(rezept2.getId())).isNotEmpty();
		//Else all appointments and prescription should be deleted
		assertThat(rezeptManagement.deletePrescription(rezept3))
				.isEqualTo("redirect:/rezepte");
		assertThat(terminplanerRepository.findById(t3.getId())).isEmpty();
		assertThat(rezeptRepository.findById(rezept3.getId())).isEmpty();
		terminplanerRepository.deleteAll();
		rezeptManagement.deleteAll();
	}
	@Test
	void existsAppointmentForPersonalId(){
		//If an appointment exists for a therapist
		Personal therapist = personalRepository.getAllPersonal().get(0);
		Personal therapist2 = personalRepository.getAllPersonal().get(1);
		Patient patient = patientRepository.findAll().get(0);
		//Create an open prescription
		Rezept rezept = new Rezept(patient,personalRepository.findAll().iterator().next(),
				List.of(new LeistungInfo(leistungRepository.getAllLeistungen().get(0)
				,1,0,0)),0,"","","","");
		rezeptRepository.save(rezept);
		terminplanerRepository.save(new Termin(
				patient, therapist, rezept, rezept.getLeistungInfoList().get(0).getLeistung(),
				0,0,0,LocalDate.now()
		));
		assertThat(rezeptManagement.existsAppointmentForPersonalId(therapist.getId())).isTrue();
		assertThat(rezeptManagement.existsAppointmentForPersonalId(therapist2.getId())).isFalse();
		terminplanerRepository.deleteAll();
		rezeptRepository.deleteAll();
	}
	@Transactional
	@Test
	void existsOpenAppointmentForPersonal(){
		Personal therapist = personalRepository.getAllPersonal().get(0);
		Personal therapist2 = personalRepository.getAllPersonal().get(1);
		Patient patient = patientRepository.findAll().get(0);
		LeistungInfo l1 = new LeistungInfo(leistungRepository.getAllLeistungen().get(0)
						,1,0,0);
		LeistungInfo l2 = new LeistungInfo(leistungRepository.getAllLeistungen().get(1)
						,1,0,1);
		leistungInfoRepository.save(l1);
		leistungInfoRepository.save(l2);
		List<LeistungInfo> list = new LinkedList<>();
		list.add(l1);
		list.add(l2);
		Rezept rezept = new Rezept(patient,personalRepository.findAll().iterator().next(),list
				,0,"","","","");
		rezeptRepository.save(rezept);
		Termin t1 = new Termin(
				patient, therapist, rezept, rezept.getLeistungInfoList().get(0).getLeistung(),
				0,0,0,LocalDate.now()
		);
		Termin t2 = new Termin(
				patient, therapist2, rezept, rezept.getLeistungInfoList().get(1).getLeistung(),
				1,0,0,LocalDate.now()
		);
		terminplanerRepository.save(t1);
		terminplanerRepository.save(t2);
		assertThat(rezeptManagement.existsOpenAppointmentForPersonal(therapist.getId())).isTrue();
		assertThat(rezeptManagement.existsOpenAppointmentForPersonal(therapist2.getId())).isFalse();
		terminplanerRepository.deleteAll();
		rezeptRepository.deleteAll();
	}

	@Transactional
	@Test
	void deletePersonal() {
		Leistung le = leistungRepository.getAllLeistungen().get(0);
		//therapist
		Personal pers1 = new Personal("","", LocalDate.now(),
				null, null, null, "",
				passwordEncoder.encode(""), null, null, "therapist");
		//recipient
		Personal pers2 = new Personal("","", LocalDate.now(),
				null, null, null, "",
				passwordEncoder.encode(""), null, null, "personal");
		//recipient
		Personal pers3 = new Personal("","", LocalDate.now(),
				null, null, null, "",
				passwordEncoder.encode(""), null, null, "personal");
		//recipient
		Personal pers4 = new Personal("","", LocalDate.now(),
				null, null, null, "",
				passwordEncoder.encode(""), null, null, "personal");
		List.of(pers1,pers2,pers3,pers4).forEach(personalRepository::save);
		Patient p1 = patientRepository.findAll().stream().findFirst().orElseThrow();
		LeistungInfo l1 = new LeistungInfo(le, 1,0,0);
		LeistungInfo l2 = new LeistungInfo(le, 2,0,0);
		LeistungInfo l3 = new LeistungInfo(le, 2,0,0);
		List.of(l1,l2,l3).forEach(leistungInfoRepository::save);
		Rezept rezept = new Rezept(
				p1, pers2, List.of(l1), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		//pers3 (recipient) has two closed prescriptions ('Abgerechnet')
		Rezept rezept2 = new Rezept(
				p1, pers3, List.of(l2), 1, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		Rezept rezept3 = new Rezept(
				p1, pers3, List.of(l3), 1, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		List.of(rezept, rezept2, rezept3).forEach(rezeptRepository::save);
		Termin t = new Termin(p1,pers1,rezept2,l2.getLeistung(),0,0,1,LocalDate.now());
		terminplanerRepository.save(t);
		//If the personel (recipient) doesn't have any prescription
		assertThat(rezeptManagement.deletePersonal(pers4.getId()))
		.isEqualTo("redirect:/personal");
		assertThat(personalRepository.findById(pers4.getId()))
				.isEmpty();
		//If an open prescription exists for the personal
		assertThat(rezeptManagement.deletePersonal(pers2.getId()))
				.isEqualTo("redirect:/error/deletePersonal/closePrescriptionMsg/"+pers2.getId());
		assertThat(personalRepository.findById(pers2.getId()))
				.isNotEmpty();
		//Else all appointments and prescriptions for the personal will be deleted
		assertThat(rezeptManagement.deletePersonal(pers3.getId()))
				.isEqualTo("redirect:/personal");
		assertThat(rezeptManagement.findById(rezept2.getId()))
				.isEmpty();
		assertThat(rezeptManagement.findById(rezept3.getId()))
				.isEmpty();
		terminplanerRepository.deleteAll();
		rezeptManagement.deleteAll();
		personalRepository.delete(pers1);
		personalRepository.delete(pers2);
	}

	@Test
	@Transactional
	void findAllPrescriptionForPersonalId() {
		Patient p1 = patientRepository.findAll().get(0);
		Personal personal = personalRepository.getAllPersonal().get(1);//recipient
		LeistungInfo l1 = new LeistungInfo(leistungRepository.getAllLeistungen().get(0), 1,0,0);
		LeistungInfo l2 = new LeistungInfo(leistungRepository.getAllLeistungen().get(0),2,0,0);
		LeistungInfo l3 = new LeistungInfo(leistungRepository.getAllLeistungen().get(1), 2,0,0);
		List.of(l1,l2,l3).forEach(leistungInfoRepository::save);
		Rezept rezept = new Rezept(
				p1, personal, List.of(l1), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		Rezept rezept2 = new Rezept(
				p1, personal, List.of(l2), 1, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		Rezept rezept3 = new Rezept(
				p1, personal, List.of(l3), 1, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		List.of(rezept, rezept2, rezept3).forEach(rezeptRepository::save);
		assertThat(rezeptManagement.findAllPrescriptionForPersonalId(personal.getId()).size())
				.isEqualTo(3);

		rezeptRepository.deleteAll();
		leistungInfoRepository.deleteAll();
	}

	@Test
	@Transactional
	void deleteLeistung() {
		Leistung leistung1 = new Leistung("le1",200,"");
		Leistung leistung2 = new Leistung("le2",300,"");
		Leistung leistung3 = new Leistung("le3",400,"");
		List.of(leistung1,leistung2,leistung3).forEach(leistungRepository::save);

		Patient p1 = patientRepository.findAll().get(0);
		Personal personal = personalRepository.getAllPersonal().get(1);//recipient

		LeistungInfo l2 = new LeistungInfo(leistung2,2,0,0);
		LeistungInfo l3 = new LeistungInfo(leistung2, 2,0,0);
		LeistungInfo l4 = new LeistungInfo(leistung3, 2,0,0);
		List.of(l2,l3,l4).forEach(leistungInfoRepository::save);

		Rezept rezept1 = new Rezept(
				p1, personal, List.of(l2), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		Rezept rezept2 = new Rezept(
				p1, personal, List.of(l3), 1, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		Rezept rezept3 = new Rezept(
				p1, personal, List.of(l4), 1, "000000000000",
				"0000000000000", "Bayern", "Rentner");

		List.of(rezept1, rezept2, rezept3).forEach(rezeptRepository::save);
		//Appointment for leistung3
		Termin t = new Termin (p1, personalRepository.getAllPersonal().get(0),
				rezept3, leistung3, 1,0,1,LocalDate.now());
		terminplanerRepository.save(t);
		//If there is no prescription for the service
		assertThat(rezeptManagement.deleteLeistung(leistung1.getId()))
				.isEqualTo("redirect:/Katalogleistungen");
		assertThat(leistungService.findLeistungById(leistung1.getId()))
				.isNull();
		//If there is an open prescription
		assertThat(rezeptManagement.deleteLeistung(leistung2.getId()))
				.isEqualTo("redirect:/error/deleteLeistung/closePrescriptionMsg/"+leistung2.getId());
		assertThat(leistungService.findLeistungById(leistung2.getId()))
				.isNotNull();
		//If else
		assertThat(rezeptManagement.deleteLeistung(leistung3.getId()))
				.isEqualTo("redirect:/Katalogleistungen");
		assertThat(leistungService.findLeistungById(leistung3.getId()))
				.isNull();
		assertThat(rezeptManagement.findById(rezept1.getId())).isNotEmpty();
		assertThat(rezeptManagement.findById(rezept2.getId())).isNotEmpty();
		assertThat(terminplanerRepository.findById(t.getId())).isEmpty();
		terminplanerRepository.deleteAll();
		rezeptRepository.deleteAll();
		leistungRepository.delete(leistung2);
		leistungInfoRepository.deleteAll();
	}

	@Transactional
	@Test
	void findAllPrescriptionForLeistungId() {
		Leistung leistung1 = new Leistung("le1",200,"");
		Leistung leistung2 = new Leistung("le2",300,"");
		Leistung leistung3 = new Leistung("le3",400,"");
		List.of(leistung1,leistung2,leistung3).forEach(leistungRepository::save);

		Patient p1 = patientRepository.findAll().get(0);
		Personal personal = personalRepository.getAllPersonal().get(1);//recipient

		LeistungInfo l2 = new LeistungInfo(leistung2,2,0,0);
		LeistungInfo l3 = new LeistungInfo(leistung2, 2,0,0);
		LeistungInfo l4 = new LeistungInfo(leistung3, 2,0,0);
		List.of(l2,l3,l4).forEach(leistungInfoRepository::save);

		Rezept rezept1 = new Rezept(
				p1, personal, List.of(l2), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		Rezept rezept2 = new Rezept(
				p1, personal, List.of(l3), 1, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		Rezept rezept3 = new Rezept(
				p1, personal, List.of(l4), 1, "000000000000",
				"0000000000000", "Bayern", "Rentner");

		List.of(rezept1, rezept2, rezept3).forEach(rezeptRepository::save);
		//leistung2 has two prescriptions (rezept1 and rezept2)
		assertThat(rezeptManagement.findAllPrescriptionForLeistungId(leistung2.getId()).size())
				.isEqualTo(2);
		//leistung3 has one prescription (rezept3)
		assertThat(rezeptManagement.findAllPrescriptionForLeistungId(leistung3.getId()).size())
				.isEqualTo(1);
		//leistung1 has no
		assertThat(rezeptManagement.findAllPrescriptionForLeistungId(leistung1.getId()).size())
				.isEqualTo(0);
		rezeptManagement.deleteAll();
		leistungRepository.delete(leistung1);
		leistungRepository.delete(leistung2);
		leistungRepository.delete(leistung3);
		leistungInfoRepository.deleteAll();
	}
}