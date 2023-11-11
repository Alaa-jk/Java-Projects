package kickstart.rezepterfassung;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

public interface AbrechnungEntryRepository  extends CrudRepository<AbrechnungEntry,Long> {
	static final Sort DEFAULT_SORT_STR = Sort.sort(AbrechnungEntry.class).by(AbrechnungEntry::getHealthInsurance).ascending();
	static final Sort DEFAULT_SORT_LONG = Sort.sort(AbrechnungEntry.class).by(AbrechnungEntry::getPatientId).ascending();

	static final Sort DEFAULT_SORT_LONG_PERSONAL = Sort.sort(AbrechnungEntry.class).by(AbrechnungEntry::getPersonalId).ascending();
	static final Sort DEFAULT_SORT_LONG_LEISTUNG = Sort.sort(AbrechnungEntry.class).by(AbrechnungEntry::getLeistungId).ascending();
	Iterable<AbrechnungEntry> findByHealthInsurance(String healthInsurance, Sort sort);

	default Iterable<AbrechnungEntry> findByHealthInsurance(String healthInsurance) {

		return findByHealthInsurance(healthInsurance, DEFAULT_SORT_STR);
	}

	Iterable<AbrechnungEntry> findByPatientId(long patientId, Sort sort);

	default Iterable<AbrechnungEntry> findByPatientId(long patientId) {

		return findByPatientId(patientId, DEFAULT_SORT_LONG);
	}
	Iterable<AbrechnungEntry> findByPersonalId(long personalId, Sort sort);

	default Iterable<AbrechnungEntry> findByPersonalId(long personalId) {

		return findByPersonalId(personalId, DEFAULT_SORT_LONG_PERSONAL);
	}
	Iterable<AbrechnungEntry> findByLeistungId(long leistungId, Sort sort);

	default Iterable<AbrechnungEntry> findByLeistungId(long leistungId) {

		return findByLeistungId(leistungId, DEFAULT_SORT_LONG_LEISTUNG);
	}
}
