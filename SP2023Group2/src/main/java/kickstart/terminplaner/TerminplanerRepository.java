package kickstart.terminplaner;

import java.time.LocalDate;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface TerminplanerRepository extends CrudRepository<Termin, Long> {
	static final Sort DEFAULT_SORT = Sort.sort(Termin.class).by(Termin::getBeginSlotIndex).ascending();

	Iterable<Termin> findByDate(LocalDate date, Sort sort);

	default Iterable<Termin> findByDate(LocalDate date) {
		return findByDate(date, DEFAULT_SORT);
	}

}