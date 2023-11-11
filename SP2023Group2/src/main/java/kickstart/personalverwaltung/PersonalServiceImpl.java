package kickstart.personalverwaltung;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import kickstart.patientenverwaltung.Patient;
import kickstart.patientenverwaltung.PatientRepository;
import kickstart.personalverwaltung.*;
//import lombok.RequiredArgsConstructor;


//


@Service
//@RequiredArgsConstructor
public class PersonalServiceImpl implements PersonalService ,UserDetailsService {
	
	@Autowired
	private PasswordEncoder passwordEncoder ;

	@Autowired
	private PersonalRepository personalRepository ;
	@Autowired
	private  PatientRepository patientRepository;
	
	
	public PersonalServiceImpl() {
	}


	public PersonalServiceImpl(PersonalRepository personalRepository,PasswordEncoder passwordEncoder, PatientRepository patientRepository) {
		super();
		this.personalRepository = personalRepository;
		this.passwordEncoder=passwordEncoder;
		this.patientRepository=patientRepository;
	}

//	
//	public PersonalServiceImpl() {
//		this.passwordEncoder = null;
//		this.personalRepository = null;
//	
//	}

	@Override
	public List<Personal> getAllPersonals() {
		  System.out.println("findall");
		  
		return  personalRepository.getAllPersonal();
	}

	@Override
	public Personal savePersonal(Personal personal) {
		return personalRepository.save(personal);
	}

	@Override
	public Personal getPersonalById(Long id) {
		return personalRepository.findById(id).get();
	}



	@Override
	public Personal updatePersonal(Personal personal) {
		return personalRepository.save(personal);
	}

	@Override
	public void deletePersonalById(Long id) {
		personalRepository.deleteById(id);	
	}

@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
	
	Optional<Personal> personal=personalRepository.getPersonalByusername(username);

	System.out.println("asdsadsad");
	
	if	(personal.isEmpty()) {
		
		throw new UsernameNotFoundException("The personal nicht vorhanden");
	}
	
		
		return new PersonalProncipile(personal);

		
}



}


	
