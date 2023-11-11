package kickstart.rezepterfassung;

import kickstart.leistungskatalog.LeistungRepository;
import kickstart.patientenverwaltung.Patient;
import kickstart.patientenverwaltung.PatientRepository;
import kickstart.personalverwaltung.Personal;
import kickstart.personalverwaltung.PersonalRepository;
import kickstart.personalverwaltung.PersonalService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class RezeptControllerUnitTest1 {
	@Autowired
	private AbrechnungEntryRepository abrechnungEntryRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RezeptController rezeptController;
	@Autowired
	private RezeptRepository rezeptRepository;
	@Autowired
	private PatientRepository patientRepository;
	@Autowired
	private PersonalService personalService;
	@Autowired
	private RezeptManagement rezeptManagement;
	@Autowired
	private LeistungRepository leistungRepository;
	@Autowired
	private PersonalRepository personalRepository;
	protected MockHttpSession session;

	protected void startSession() {
		session = new MockHttpSession();
	}

	protected void endSession() {
		session.clearAttributes();
		session = null;
	}
	@BeforeEach
	void setUp() {
		startSession();
	}

	@AfterEach
	void tearDown() {
		endSession();
	}

	@Test
	@WithMockUser(authorities = {"boss","personal"})
	void serveList() throws Exception {
		rezeptController.serveList(session);
		List<Rezept> rezepte = (List<Rezept>)session.getAttribute("rezepte");
		rezepte.forEach(r -> {
			assertThat(r.getStatus()).isEqualTo(0);
		});
	}

	@Test
	@WithMockUser(authorities = {"boss","personal"})
	void serveForm() {
		rezeptController.serveForm(session);
		assertThat(session.getAttribute("patientMap")).isNotNull();
		assertThat(session.getAttribute("personalMap")).isNotNull();
	}
	@Test
	@WithMockUser(authorities = {"boss","personal"})
	void settlePrescription(){
		Rezept r = new Rezept();
		rezeptRepository.save(r);
		long id = r.getId();
		rezeptController.settlePrescription(session, id+"");
		assertThat(rezeptRepository.findById(id).get().getStatus())
				.isEqualTo(1);
	}
	@Test
	void showEntriesForHI() {
		AbrechnungEntry a1 = new AbrechnungEntry();
		AbrechnungEntry a2 = new AbrechnungEntry();
		a1.setHealthInsurance("AOK");
		a1.setArchived(true);
		a1.setServiceProvided(true);
		a2.setHealthInsurance("Bayern");
		a2.setArchived(true);
		a2.setServiceProvided(true);
		List.of(a1,a2).forEach(abrechnungEntryRepository::save);

		rezeptController.showEntriesForHI(session,"AOK");
		HashMap<List<AbrechnungEntry>,Long> rezepteFinished
				= (HashMap<List<AbrechnungEntry>,Long>) session.getAttribute("rezepteFinished");
		for(List<AbrechnungEntry> e : rezepteFinished.keySet()){
			for(AbrechnungEntry a : e ){
				assertThat(a.getHealthInsurance()).isEqualTo("AOK");
			}
		}
		assertThat(rezepteFinished.size())
				.isEqualTo(1);
		abrechnungEntryRepository.deleteAll();
	}
	@Test
	void formatEuroWithColon(){
		AtomicInteger sum = new AtomicInteger(1000);
		assertThat(rezeptController.formatEuroWithColon(sum))
				.isEqualTo("10,00");
	}
	//After the operation a new Rezept should be created with the id incremented by 1
	@Test
	@WithMockUser(authorities = {"boss","personal"})
	void fillPatientInformation() {
		Rezept rezept1 = new Rezept();
		rezeptRepository.save(rezept1);
		Patient patient = new Patient("", "",LocalDate.now(),
				"","","");
		patientRepository.save(patient);
		long patientId = patient.getId();
		Personal personal = new Personal("B","Freund", LocalDate.now(),
				null, null, null, "M",
				passwordEncoder.encode("password"), null, null, "personal");
		personalService.savePersonal(personal);
		long personalId = personal.getId();
		HashMap<String,String> requestMap
				= new HashMap<>();
		requestMap.put("patientId",patientId+"");
		requestMap.put("personalId",personalId+"");
		requestMap.put("approvalNumber","");
		requestMap.put("diagnosisNumber","");
		requestMap.put("province","");
		requestMap.put("personalGroup","");
		rezeptController.fillPatientInformation(requestMap,session);
		assertThat(session.getAttribute("rid").toString())
				.isEqualTo((1+rezept1.getId())+"");
		rezeptRepository.deleteAll();
		patientRepository.delete(patient);
		personalService.deletePersonalById(personalId);
	}
}
