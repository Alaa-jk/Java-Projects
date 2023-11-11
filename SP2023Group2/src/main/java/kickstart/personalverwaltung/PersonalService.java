package kickstart.personalverwaltung;

import java.util.List;

public interface PersonalService {
    List<Personal> getAllPersonals();
	
	Personal savePersonal(Personal personal);
	
	Personal getPersonalById(Long id);
	
	Personal updatePersonal(Personal personal);
	
	void deletePersonalById(Long id);


}
