package kickstart.leistungskatalog;


import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;




public interface LeistungRepository extends CrudRepository<Leistung, Long> {

	@Modifying
	@Query(value="SELECT * FROM Leistung  ", nativeQuery = true)
	List<Leistung> getAllLeistungen();

	@Query(value = "SELECT l FROM Leistung l  where l.id = :id")
	public Leistung getLeistungbyId(Long id);


//	  @Query("DELETE FROM Leistung l WHERE l.id = :id")
//	    public void deleteLeistungById(Long id);


	@Query("UPDATE Leistung l SET l.name =:name, l.prise = :prise ,l.dauer =:dauer WHERE l.id = :id")
	void updateLeistungById(Long id, String name, double prise,String dauer);


	@Modifying
	@Query(value ="Leistung l SET l.name =:name, l.prise = :prise ,l.dauer =:dauer WHERE l.id = :id", nativeQuery = true)
	public void updateleistung(@Param("id")Long id,@Param("name") String name,@Param("prise") double prise,@Param("dauer") String dauer);




}