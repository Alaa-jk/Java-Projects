package kickstart.roles;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;




@Component
public interface RolesRepo  extends CrudRepository<Role, Long>{

	@Modifying
	@Query(value ="SELECT * FROM Role  ", nativeQuery = true)
	public List<Role> getAllRole();

	@Modifying
	@Query(value ="UPDATE Role l SET l.role =:role  WHERE l.id = :id", nativeQuery = true)
	public void updateRoleById(@Param("id")Long id,@Param("role") String role);

	  @Modifying
	  @Query(value ="DELETE FROM Leistung l WHERE l.id = :id", nativeQuery = true)
       public void deleteRoleById(Long id);




}
