package kickstart.rezepterfassung;

import kickstart.leistungskatalog.Leistung;
import kickstart.leistungskatalog.LeistungRepository;
import kickstart.leistungskatalog.LeistungService;
import kickstart.patientenverwaltung.Patient;
import kickstart.patientenverwaltung.PatientRepository;
import kickstart.personalverwaltung.Personal;
import kickstart.personalverwaltung.PersonalRepository;
import kickstart.terminplaner.Termin;
import kickstart.terminplaner.TerminplanerRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;

import static org.assertj.core.api.Assertions.*;
import java.time.LocalDate;
import java.util.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class RezeptControllerUnitTest2 {
	@Autowired
	private TerminplanerRepository terminplanerRepository;
	@Autowired
	private LeistungService leistungService;
	@Autowired
	private AbrechnungEntryRepository abrechnungEntryRepository;
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

	@Autowired
	private PasswordEncoder passwordEncoder;
	protected MockHttpSession session;
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
				new Patient("PAF1","PAL1", LocalDate.now(),"","",""),
				new Patient("PAF2","PAL2", LocalDate.now(),"","",""),
				new Patient("PAF3","PAL3", LocalDate.now(),"","","")
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
		endSession();
	}

	@Transactional
	@Test
	void fillLeistungInfo() {
		Rezept rezept = new Rezept(patientRepository.findAll().stream().findFirst().orElseThrow(),
				personalRepository.findAll().iterator().next(),
				new LinkedList<LeistungInfo>(), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptRepository.save(rezept);

		String countService = 2+"";
		Map<String,String> reqParam = new HashMap<>();
		reqParam.put("serviceId","1");
		session.setAttribute("rid",rezept.getId()+"");
		reqParam.put("countService",countService);
		rezeptController.fillLeistungInfo(session,reqParam);
		assertThat(
				rezeptManagement.findById(rezept.getId()).map(r -> {
					return r.getLeistungInfoList().size();
				}).orElseThrow()).isEqualTo(1);
		rezeptRepository.deleteAll();
		leistungInfoRepository.deleteAll();
	}

	@Test
	@WithMockUser(authorities = {"boss"})
	void removeEntry() {
		Rezept rezept = new Rezept(patientRepository.findAll().stream().findFirst().orElseThrow(),
				personalRepository.findAll().iterator().next(),
				new LinkedList<LeistungInfo>(), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptManagement.save(rezept);
		long id = rezept.getId();
		rezeptController.removeEntry(Optional.of(rezept));
		assertThat(rezeptManagement.findById(id).isPresent()).isFalse();
	}
	@Transactional
	@Test
	@WithMockUser(authorities = {"boss"})
	void removeService() {
		Rezept rezept = new Rezept(patientRepository.findAll().stream().findFirst().orElseThrow(),
				personalRepository.findAll().iterator().next(),
				new LinkedList<LeistungInfo>(), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptManagement.save(rezept);
		long rezeptId = rezept.getId();
		rezeptManagement.addLeistungInfoToList(rezeptId,leistungRepository
				.getAllLeistungen()
				.stream()
				.findAny()
				.map(l -> l.getId()).orElseThrow(),1);
		rezeptController.removeService(rezeptId+"",
				rezeptManagement.findById(rezeptId)
						.map(r->{ return r.getLeistungInfoList().get(0); }));
		assertThat(rezeptManagement.getLeistungInfoListById(rezeptId).size()).isEqualTo(0);
		rezeptManagement.deleteAll();
		leistungInfoRepository.deleteAll();
	}
	@Test
	@WithMockUser(authorities = {"boss","personal"})
	void showServicesUpdateForm() {
		Rezept rezept = new Rezept(patientRepository.findAll().stream().findFirst().orElseThrow(),
				personalRepository.findAll().iterator().next(),
				new LinkedList<LeistungInfo>(), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptManagement.save(rezept);
		long rezeptId = rezeptManagement.findAll().iterator().next().getId();
		rezeptController.showServicesUpdateForm(session,rezeptId+"");
		assertThat(rezeptId).isEqualTo(((Rezept)session.getAttribute("rezept")).getId());
		rezeptManagement.deleteAll();
	}

	@Transactional
	@Test()
	@WithMockUser(authorities = {"boss","personal"})
	void addService(){
		Rezept rezept = new Rezept(patientRepository.findAll().stream().findFirst().orElseThrow(),
				personalRepository.findAll().iterator().next(),
				new ArrayList<LeistungInfo>(List.of(new LeistungInfo(
						leistungRepository.getAllLeistungen()
								.stream().findFirst().orElseThrow()
				,5,0,0))), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptRepository.save(rezept);

		String countService = 2+"";
		Map<String,String> reqParam = new HashMap<>();
		reqParam.put("serviceId","1");
		reqParam.put("countService",countService);
		rezeptController.addService(reqParam,session,rezept.getId()+"");
		assertThat(rezept.getId()).isEqualTo(((Rezept)session.getAttribute("rezept")).getId());
		rezeptManagement.deleteAll();
	}
	@Test
	@WithMockUser(authorities = {"boss","personal"})
	void updateEntry(){
		Rezept rezept = new Rezept(patientRepository.findAll().stream().findFirst().orElseThrow(),
				personalRepository.findAll().iterator().next(),
				List.of(new LeistungInfo(
						leistungRepository.getAllLeistungen()
								.stream().findFirst().map(l -> {return l;}).orElseThrow()
						,5,0,0)), 0, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptRepository.save(rezept);

		Map<String,String> reqParam = new HashMap<>();
		reqParam.put("approvalNumber","1");
		reqParam.put("diagnosisNumber","1");
		reqParam.put("province","Sachsen");
		reqParam.put("personalGroup","Familienmitglieder");
		rezeptController.updateEntry(reqParam,rezept.getId()+"");
		assertThat(rezeptManagement.findById(rezept.getId())
				.map(r -> { return r.getApprovalNumber(); }).orElseThrow())
				.isEqualTo("1");
		rezeptManagement.deleteAll();
	}

	@Test
	@WithMockUser(authorities = {"boss","personal"})
	void closePrescriptionMsg() {
		long patientId = patientRepository.findAll().get(0).getId();
		assertThat(rezeptController.closePrescriptionMsg(""+patientId,new ExtendedModelMap()))
				.isEqualTo("closePrescriptionMsg");
	}

	@Test
	@Transactional
	void updateAbrechnungEntries() {
		LeistungInfo li1 = new LeistungInfo(leistungService.getAllLeistungen().get(0), 5,0,2);
		LeistungInfo li2 = new LeistungInfo(leistungService.getAllLeistungen().get(1), 5,0,0);
		List<LeistungInfo> list = new LinkedList<>();
		List.of(li1, li2).forEach(leistungInfoRepository::save);
		list.addAll(List.of(li1,li2));
		Rezept rezept = new Rezept(
				patientRepository.findAll().get(0),
				personalRepository.getAllPersonal().get(0),
				list,
				1, "000000000000",
				"0000000000000", "Bayern", "Rentner");
		rezeptRepository.save(rezept);
		//Create two AbrechnungEntry (archived/not archived) for the prescription
		AbrechnungEntry a1 = new AbrechnungEntry(
				patientRepository.findAll().get(0).getFirstname(),
				patientRepository.findAll().get(0).getLastname(),
				LocalDate.now(), patientRepository.findAll().get(0).getId(),
				personalRepository.getAllPersonal().get(0).getFirstname(),
				personalRepository.getAllPersonal().get(0).getLastname(),
				LocalDate.now(), personalRepository.getAllPersonal().get(0).getId(),
				rezept.getId(), li1.getLeistung().getId(), li1.getLeistung().getName(),
				li1.getPriceInCent(), li1.getActCount(),
				li1.getActCount() > 0, "", "", "", "",
				"", true
		);
		AbrechnungEntry a2 = new AbrechnungEntry(
				patientRepository.findAll().get(0).getFirstname(),
				patientRepository.findAll().get(0).getLastname(),
				LocalDate.now(), patientRepository.findAll().get(0).getId(),
				personalRepository.getAllPersonal().get(0).getFirstname(),
				personalRepository.getAllPersonal().get(0).getLastname(),
				LocalDate.now(), personalRepository.getAllPersonal().get(0).getId(),
				rezept.getId(), li2.getLeistung().getId(), li2.getLeistung().getName(),
				li2.getPriceInCent(), li2.getActCount(),
				li2.getActCount() > 0, "", "", "", "",
				"", false
		);
		List.of(a1,a2).forEach(abrechnungEntryRepository::save);
		patientRepository.findAll().get(0).setHealthInsurance("AOK");
		rezeptController.updateAbrechnungEntries();
		assertThat(abrechnungEntryRepository.findById(a1.getId()).orElseThrow().getHealthInsurance())
				.isEqualTo("");
		assertThat(abrechnungEntryRepository.findById(a2.getId()).orElseThrow().getHealthInsurance())
				.isEqualTo("AOK");
		rezeptManagement.delete(rezept);
		leistungInfoRepository.delete(li1);
		leistungInfoRepository.delete(li2);
		abrechnungEntryRepository.deleteAll();
	}

	@WithMockUser(authorities = {"boss","personal"})
	@Test
	void closePrescriptionMsgPersonal() {
		long personalId = personalRepository.getAllPersonal().get(0).getId();
		assertThat(rezeptController.closePrescriptionMsgPersonal(""+personalId,new ExtendedModelMap()))
				.isEqualTo("closePrescriptionMsgPersonal");
	}
	@WithMockUser(authorities = {"boss","personal"})
	@Test
	void closePrescriptionMsgLeistung() {
		long leistungId = leistungService.findAll().iterator().next().getId();
		assertThat(rezeptController.closePrescriptionMsgLeistung(""+leistungId,new ExtendedModelMap()))
				.isEqualTo("closePrescriptionMsgLeistung");
	}
	@WithMockUser(authorities = {"boss","personal"})
	@Test
	void cannotClosePrescriptionMsg() {
		Rezept rezept = new Rezept();
		rezeptRepository.save(rezept);
		assertThat(rezeptController.cannotClosePrescriptionMsg(""+rezept.getId(),new ExtendedModelMap()))
				.isEqualTo("cannotDeletePrescriptionMsg");
		rezeptRepository.delete(rezept);
	}
	@WithMockUser(authorities = {"boss","personal"})
	@Test
	void deleteCompletedAppointments() {
		Rezept rezept = new Rezept();
		rezeptRepository.save(rezept);
		Termin t1 = new Termin();
		Termin t2 = new Termin();
		t1.setRezept(rezept);
		t1.setStatus(1);
		t2.setRezept(rezept);
		t2.setStatus(1);
		List.of(t1,t2).forEach(terminplanerRepository::save);
		assertThat(rezeptController.deleteCompletedAppointments(rezept.getId()+""))
				.isEqualTo("redirect:/rezepte");
		assertThat(rezeptManagement.findById(rezept.getId())).isEmpty();
		assertThat(terminplanerRepository.findById(t1.getId())).isEmpty();
		assertThat(terminplanerRepository.findById(t2.getId())).isEmpty();
	}
	@WithMockUser(authorities = {"boss","personal"})
	@Test
	void cannotUpdateLeistungMsg() {
		Leistung leistung = new Leistung();
		leistungRepository.save(leistung);
		assertThat(rezeptController.cannotUpdateLeistungMsg(""+leistung.getId(),new ExtendedModelMap()))
				.isEqualTo("cannotUpdateLeistungMsg");
		leistungRepository.delete(leistung);
	}

	@WithMockUser(authorities = {"boss","personal"})
	@Test
	void cannotDeleteTherapistMsg() {
		Personal personal = personalRepository.getAllPersonal().get(0);
		assertThat(rezeptController.cannotDeleteTherapistMsg(""+personal.getId(),new ExtendedModelMap()))
				.isEqualTo("closeAppointmentMsg");
	}

	@WithMockUser(authorities = {"boss","personal"})
	@Test
	void confirmPayment(){
		AbrechnungEntry a1 = new AbrechnungEntry();
		AbrechnungEntry a2 = new AbrechnungEntry();
		a1.setRezeptId(1);
		a2.setRezeptId(1);
		List.of(a1,a2).forEach(abrechnungEntryRepository::save);
		rezeptController.confirmPayment(""+1,session);
		for(AbrechnungEntry a : abrechnungEntryRepository.findAll()) {
			assertThat(a.isConfirmed()).isTrue();
		}
		abrechnungEntryRepository.delete(a1);
		abrechnungEntryRepository.delete(a2);
	}
}
