package kickstart.personalverwaltung;

import java.util.List;

import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Order(1)
public class Datainitialzer implements ApplicationRunner {

	private static final Logger LOG = LoggerFactory.getLogger(DataInitializer.class);

	private final PersonalRepository personalRepository;

	private final PasswordEncoder passwordEncoder;

	public Datainitialzer(PersonalRepository personalRepository, PasswordEncoder passwordEncoder) {

		Assert.notNull(personalRepository, "PatientRepository must not be null!");
		this.personalRepository = personalRepository;
		this.passwordEncoder = passwordEncoder;

		System.out.println("Hello Rle");
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		LOG.info("Creating default personnel.");
		List.of(
				new Personal("Boss", " ", null,
						null, null, null,
						"boss", passwordEncoder.encode("boss"),
						null, null, "boss"),
				new Personal("Max", "Müller", null,
						null, null, null,
						"personal", passwordEncoder.encode("123"),
						null, null, "personal"),
				new Personal("Adam","Selipsky", null,
						null, null, null, "personal1",passwordEncoder.encode("password"), null, null, "personal"),
				new Personal("Michael","Ende", null,
								null, null, null, "therapist1",passwordEncoder.encode("password"), null, null, "therapeut"),
				new Personal("Jessy", "Friedrich", null,
						null, null, null,
						"therapeuten", passwordEncoder.encode("123"),
						null, null, "therapeut"))
				.forEach(personalRepository::save);

	}

}
