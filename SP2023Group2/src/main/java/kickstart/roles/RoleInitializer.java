package kickstart.roles;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class RoleInitializer implements ApplicationRunner {

	
	@Autowired 
	private RolesRepo rolesRepo;

	public RoleInitializer(RolesRepo rolesRepo) {
		super();
		this.rolesRepo = rolesRepo;
		System.out.println("Hello Rle");
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List.of(
				new Role("boss"),
				new Role("personal"),
				new Role("therapeut")
		).forEach(rolesRepo::save);
		
		
		
	}
	}


