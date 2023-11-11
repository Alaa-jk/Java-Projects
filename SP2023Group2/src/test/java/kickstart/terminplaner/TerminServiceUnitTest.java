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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class TerminServiceUnitTest {
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
	void tearDownEach(){

		terminplanerRepository.deleteAll();
		endSession();
	}

	@Test
	void isWithinTimeslots() {
		Leistung leistung = leistungRepository.getAllLeistungen().stream().findFirst().orElseThrow();
		Rezept rezept = new Rezept(patientRepository.findAll().stream().findFirst().orElseThrow(),
				personalRepository.findAll().iterator().next(),
				List.of(new LeistungInfo(
						leistungRepository.getAllLeistungen()
								.stream().findFirst().map(l -> {return l;}).orElseThrow(),
						5,0,0)), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptRepository.save(rezept);
		Termin t = new Termin(
				patientRepository.findAll().stream().findFirst().orElseThrow(),
				personalRepository.findAll().iterator().next(),
				rezept, leistung,0,0,1, LocalDate.now());
		assertThat(terminService.isWithinTimeslots(t)).isEqualTo(true);
		Termin t2 = new Termin(
				patientRepository.findAll().stream().findFirst().orElseThrow(),
				personalRepository.findAll().iterator().next(),
				rezept, leistung,0,11,3, LocalDate.now());
		assertThat(terminService.isWithinTimeslots(t2)).isEqualTo(false);
	}

	@Test
	void hasCollision() {
		Leistung leistung = leistungRepository.getAllLeistungen().stream().findFirst().orElseThrow();
		Rezept rezept = new Rezept(patientRepository.findAll().stream().findFirst().orElseThrow(),
				personalRepository.findAll().iterator().next(),
				List.of(new LeistungInfo(
						leistungRepository.getAllLeistungen()
								.stream().findFirst().orElseThrow(),
						5,0,0)), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptRepository.save(rezept);
		terminplanerRepository.save(new Termin(
				patientRepository.findAll().stream().findFirst().orElseThrow(),
				personalRepository.findAll().iterator().next(),
				rezept, leistung,0,0,1, LocalDate.now()));
		terminplanerRepository.save(new Termin(
				patientRepository.findAll().stream().findFirst().orElseThrow(),
				personalRepository.findAll().iterator().next(),
				rezept, leistung,0,7,3, LocalDate.now().plusDays(1)));
		assertThat(terminService.hasCollision(
				new Termin(
						patientRepository.findAll().stream().findFirst().orElseThrow(),
						personalRepository.findAll().iterator().next(),
						rezept, leistung,0,10,1, LocalDate.now())
		)).isFalse();
		assertThat(terminService.hasCollision(
				new Termin(
						patientRepository.findAll().stream().findFirst().orElseThrow(),
						personalRepository.findAll().iterator().next(),
						rezept, leistung,0,9,2, LocalDate.now().plusDays(1)))
		).isTrue();
		terminplanerRepository.deleteAll();
	}

	@Test
	void recommend() {
		Leistung leistung = leistungRepository.getAllLeistungen().stream().findFirst().orElseThrow();
		Rezept rezept = new Rezept(patientRepository.findAll().stream().findFirst().orElseThrow(),
				personalRepository.findAll().iterator().next(),
				List.of(new LeistungInfo(
						leistungRepository.getAllLeistungen()
								.stream().findFirst().map(l -> {return l;}).orElseThrow(),
						5,0,0)), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptRepository.save(rezept);
		terminplanerRepository.save(new Termin(
				patientRepository.findAll().stream().findFirst().orElseThrow(),
				personalRepository.findAll().iterator().next(),
				rezept, leistung,0,0,1, LocalDate.now()));
		Map<LocalDate, Integer> map
				= terminService.recommend(new Termin(
				patientRepository.findAll().stream().findFirst().orElseThrow(),
				personalRepository.findAll().iterator().next(),
				rezept, leistung,0,0,3, LocalDate.now()));
		for(Map.Entry e : map.entrySet()){
			assertThat((e.getKey()).equals(LocalDate.now())).isEqualTo(true);
			assertThat(e.getValue()).isEqualTo(1);
		}
		terminplanerRepository.deleteAll();
	}
}