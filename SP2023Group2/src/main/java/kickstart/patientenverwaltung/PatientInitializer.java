package kickstart.patientenverwaltung;

import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import org.springframework.util.Assert;

import kickstart.personalverwaltung.Personal;
@Component
@Order(2)
public class PatientInitializer implements ApplicationRunner {

	private static final Logger LOG = LoggerFactory.getLogger(PatientInitializer.class);
	private final PatientRepository patientRepository;

	public PatientInitializer(PatientRepository patientRepository) {

		Assert.notNull(patientRepository, "PatientRepository must not be null!");
		this.patientRepository = patientRepository;
	}


	
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		LOG.info("Creating default patients");
		List.of(
				new Patient("Armon","Dadgar",
						LocalDate.of(1990,9,9),
						"AOK", "A111111111","Adresse1"),
				new Patient("Thomas","Dohmke",
						LocalDate.of(1979,1,9),
						"Techniker Krankenkasse", "A123456789","Adresse2"),
				new Patient("Larry","Page",
						LocalDate.of(1998,9,4),
						"Techniker Krankenkasse", "A223456789","Adresse3")
		).forEach(patientRepository::save);
	}
	
}
