package kickstart.rezepterfassung;

import kickstart.leistungskatalog.LeistungService;
import kickstart.patientenverwaltung.Patient;
import kickstart.patientenverwaltung.PatientServiceImpl;
import kickstart.personalverwaltung.Personal;
import kickstart.personalverwaltung.PersonalServiceImpl;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Transactional
@Component
@Order(4)
class RezeptInitializer implements ApplicationRunner {

	private static final Logger LOG = LoggerFactory.getLogger(RezeptInitializer.class);
	private static final String[] ROLES = {"boss","personal","therapeut"};
	@Autowired
	private RezeptRepository rezeptRepository;
	@Autowired
	private LeistungService leistungService;
	@Autowired
	private LeistungInfoRepository leistungInfoRepository;
	@Autowired
	private PersonalServiceImpl personalServiceImpl;
	@Autowired
	private PatientServiceImpl patientServiceImpl;

	@Override
	public void  run(ApplicationArguments args) throws Exception {
		LOG.info("Creating default prescriptions.");
		int count = 0;
		for(Patient patient : patientServiceImpl.getAllPatients()){
			for(Personal personal : personalServiceImpl.getAllPersonals()){
				if(personal.getRoles().equals(ROLES[1])) {
					List<LeistungInfo> leistungInfoList = new LinkedList<>();
					leistungService.findAll().forEach(leistung -> {
						LeistungInfo li = new LeistungInfo(leistung, 2, 0, 0);
						leistungInfoRepository.save(li);
						leistungInfoList.add(li);
					});
					rezeptRepository.save(
							new Rezept(patient, personal, leistungInfoList, 0, "11111111"+count,
									"A0" + count, "Sachsen", "Mitglied")
					);
					count++;
				}
			}
		}
	}
}
