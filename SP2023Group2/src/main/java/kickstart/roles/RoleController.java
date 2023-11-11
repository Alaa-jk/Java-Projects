package kickstart.roles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

	
	@Autowired 
	private RolesRepo rolesRepo;
	
	@Autowired
	private RoleServecies roleservecies;

	public RoleController(RolesRepo rolesRepo, RoleServecies roleservecies) {
		super();
		this.rolesRepo = rolesRepo;
		this.roleservecies = roleservecies;
	}
	

	@GetMapping("/roles")
	@PreAuthorize("hasAuthority('boss')")
	public List<Role> listroles() {
			List<Role> role=roleservecies.getAllRoles();
		
		return role;
	}
}
