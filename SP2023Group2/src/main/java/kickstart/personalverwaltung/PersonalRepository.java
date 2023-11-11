package kickstart.personalverwaltung;


import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import kickstart.leistungskatalog.Leistung;





public interface PersonalRepository extends CrudRepository<Personal, Long> {

	@Query("SELECT l FROM Personal l ")
	List<Personal> getAllPersonal();
	
	@Query(value = "SELECT * FROM Personal l  where l.username = :username limit 1", nativeQuery = true)
	public Optional<Personal>  getPersonalByusername(String username);

}