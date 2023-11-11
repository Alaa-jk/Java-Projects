package kickstart.roles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class RoleServecies {

	@Autowired 
	private RolesRepo rolesRepo;

	public RoleServecies(RolesRepo rolesRepo) {
		super();
		this.rolesRepo = rolesRepo;
	}
	
	

	public List<Role> getAllRoles() {
		return  rolesRepo.getAllRole();
	}
}
