package kickstart.terminplaner;

import kickstart.leistungskatalog.Leistung;
import kickstart.leistungskatalog.LeistungRepository;
import kickstart.patientenverwaltung.Patient;
import kickstart.patientenverwaltung.PatientRepository;
import kickstart.personalverwaltung.Personal;
import kickstart.personalverwaltung.PersonalRepository;
import kickstart.rezepterfassung.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class TerminplanerControllerUnitTest {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private TerminService terminService;
	@Autowired private TerminplanerController controller;
	@Autowired private TerminplanerRepository terminplanerRepository;
	@Autowired
	private RezeptController rezeptController;
	@Autowired
	private RezeptManagement rezeptManagement;
	@Autowired
	private RezeptRepository rezeptRepository;
	@Autowired
	private PatientRepository patientRepository;
	@Autowired
	private PersonalRepository personalRepository;
	@Autowired
	private LeistungRepository leistungRepository;
	@Autowired
	private LeistungInfoRepository leistungInfoRepository;
	protected MockHttpSession session;
	private static final String[] ROLES = new String[]{"boss","personal","therapeut"};
	@BeforeAll
	void setUp() {
		terminplanerRepository.deleteAll();
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
				new Leistung("Atemtherapie",300,"20"),
				new Leistung("Atemtherapie",400,"20"),
				new Leistung("Elektromassage",500,"20")
		).forEach(leistungRepository::save);
		List.of(
				new Patient("PAF1","PAL1", LocalDate.now(),"","",""),
				new Patient("PAF2","PAL2", LocalDate.now(),"","",""),
				new Patient("PAF3","PAL3", LocalDate.now(),"","","")
		).forEach(patientRepository::save);
		List.of(
				new Personal("boss","Freund", LocalDate.now(),
						null, null, null, "boss",
						passwordEncoder.encode("boss"), null, null, ROLES[0]),
				new Personal("Thea","Freund", LocalDate.now(),
						null, null, null, "thea",
						passwordEncoder.encode("password"), null, null, ROLES[1]),
				new Personal("Lara","Freund", LocalDate.now(),
						null, null, null, "lara",
						passwordEncoder.encode("password"), null, null, ROLES[2]),
				new Personal("Clara","Freund", LocalDate.now(),
						null, null, null, "clara",
						passwordEncoder.encode("password"), null, null, ROLES[2])
		).forEach(personalRepository::save);
	}
	protected void startSession() {
		session = new MockHttpSession();
	}

	protected void endSession() {
		session.clearAttributes();
		session = null;
	}

	@BeforeEach
	void setUpEach(){
		startSession();
	}

	@AfterEach
	void tearDownEach() {

		terminplanerRepository.deleteAll();
		endSession();
	}
	@Transactional
	@Test
	void removeEntry() {
		Leistung le1 = leistungRepository.getAllLeistungen().get(0);
		Leistung le2 = leistungRepository.getAllLeistungen().get(1);
		LeistungInfo li1 = new LeistungInfo(le1,5,0,0);
		LeistungInfo li2 = new LeistungInfo(le2,2,0,0);
		Rezept rezept = new Rezept(patientRepository.findAll().stream().findFirst().orElseThrow(),
				personalRepository.findAll().iterator().next(),
				List.of(li1, li2), 0, "000000000",
				"A00", "Bayern", "Rentner");
		rezeptRepository.save(rezept);
		session.setAttribute("date_chosen",LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		Termin t = new Termin(rezept.getPatient(),
				personalRepository.findAll().iterator().next(),
				rezept, rezept.getLeistungInfoList().stream().findFirst().orElseThrow().getLeistung(),
				0, 1, 1, LocalDate.now());
		terminplanerRepository.save(t);
		long countBefore = terminplanerRepository.count();
		controller.removeEntry(t.getId(),session);
		assertThat(countBefore - terminplanerRepository.count()).isEqualTo(1);
		rezeptRepository.delete(rezept);
		leistungInfoRepository.delete(li1);
		leistungInfoRepository.delete(li2);
	}

	@Transactional
	@Test
	void markAsCompleted(){
		Leistung le1 = leistungRepository.getAllLeistungen().get(0);
		Leistung le2 = leistungRepository.getAllLeistungen().get(1);
		LeistungInfo li1 = new LeistungInfo(le1,5,0,0);
		LeistungInfo li2 = new LeistungInfo(le2,2,0,0);
		Rezept rezept = new Rezept(patientRepository.findAll().stream().findFirst().orElseThrow(),
				personalRepository.findAll().iterator().next(),
				List.of(li1, li2), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptRepository.save(rezept);
		session.setAttribute("date_chosen",LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		Termin t = new Termin(rezept.getPatient(),
				personalRepository.findAll().iterator().next(),
				rezept, rezept.getLeistungInfoList().stream().findFirst().orElseThrow().getLeistung(),
				0, 1, 1, LocalDate.now());
		terminplanerRepository.save(t);
		controller.markAsCompleted(t.getId(),session);
		assertThat(terminplanerRepository.findById(t.getId())
				.map(Termin::getStatus).orElseThrow())
				.isEqualTo(1);
	}
	@Test
		void index() {
			assertThat(controller.index()).isEqualTo("terminplanung");
		}

	@Transactional
	@Test
	void book() {
		Leistung leistung = leistungRepository.getAllLeistungen()
				.stream().findFirst().orElseThrow();
		Personal therapist = personalRepository.findAll().iterator().next();
		Rezept rezept = new Rezept(patientRepository.findAll().stream().findFirst().orElseThrow(),
				therapist,
				List.of(new LeistungInfo(leistung,5,0,0)), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptRepository.save(rezept);
		controller.book(session, new ExtendedModelMap());
		assertThat(((List<Rezept>)(session.getAttribute("rezepte"))).size()).isEqualTo(1);
		controller.showPatientForm(rezept.getId()+"",1+"",
				LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
				therapist.getId()+"",session);
		assertThat((String)(session.getAttribute("therapistId")))
				.isEqualTo(therapist.getId()+"");
		controller.toEingabeAnzahl(leistung.getId()+"",session);
		assertThat((String)(session.getAttribute("leistungId")))
				.isEqualTo(leistung.getId()+"");
		controller.addEntry(session,1+"");
		assertThat((String)(session.getAttribute("msg")))
				.isEqualTo("Termin am "+
						LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+
						" um 10:20 Uhr wurde reserviert");
		terminplanerRepository.deleteAll();
		rezeptRepository.delete(rezept);
	}

	@Test
	void showTherapistPlanForm() {
		Model model = new ExtendedModelMap();
		controller.showTherapistPlanForm(model);
		for(Personal therapist : (List<Personal>)(model.getAttribute("therapists"))){
			assertThat(therapist.getRoles()).isEqualTo(ROLES[2]);
		}
	}

	@Test
	void showTherapistPlan() {
		Personal therapist = null;
		for (Personal personal : personalRepository.findAll()) {
			if (personal.getRoles().equals(ROLES[2])) {
				therapist = personal;
				break;
			}
		}
		Termin t1 = new Termin();
		t1.setTherapist(therapist);
		t1.setDate(LocalDate.now());
		Termin t2 = new Termin();
		t2.setTherapist(therapist);
		t2.setDate(LocalDate.now());
		Termin t3 = new Termin();
		t3.setTherapist(therapist);
		t3.setDate(LocalDate.now().plusDays(1));
		List.of(t1, t2, t3).forEach(terminplanerRepository::save);
		Model model = new ExtendedModelMap();
		controller.showTherapistPlan(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), therapist.getId() + "", model);
		assertThat(((List<Termin>) (model.getAttribute("appointments")))
				.size()).isEqualTo(2);
		terminplanerRepository.delete(t1);
		terminplanerRepository.delete(t2);
		terminplanerRepository.delete(t3);
	}

	@Test
	void showAppointmentsForm() {
		assertThat(controller.showAppointmentsForm(new ExtendedModelMap()))
				.isEqualTo("terminplanung_all_date_input");
		Model model = new ExtendedModelMap();
		controller.showAppointments(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), model);
		assertThat(LocalDate.parse(model.getAttribute("date").toString()
				,DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.isEqualTo(LocalDate.now());
		for(Termin t : (List<Termin>)(model.getAttribute("appointments"))){
			assertThat(t.getDate()).isEqualTo(LocalDate.now());
		}
	}
}