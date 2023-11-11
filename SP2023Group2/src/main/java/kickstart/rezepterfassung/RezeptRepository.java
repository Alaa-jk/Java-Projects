package kickstart.rezepterfassung;

import java.time.LocalDate;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface RezeptRepository extends CrudRepository<Rezept, Long> {
	static final Sort DEFAULT_SORT = Sort.sort(Rezept.class).by(Rezept::getHealthInsurance).ascending();

	Iterable<Rezept> findByHealthInsurance(String healthInsurance, Sort sort);

	default Iterable<Rezept> findByHealthInsurance(String healthInsurance) {

		return findByHealthInsurance(healthInsurance, DEFAULT_SORT);
	}
}