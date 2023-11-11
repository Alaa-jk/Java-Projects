package kickstart.patientenverwaltung;

import java.util.List;

public interface PatientService {
    List<Patient> getAllPatients();
	
	Patient savePatient(Patient patient);
	
	Patient getPatientById(Long id);
	
	Patient updatePatient(Patient patient);
	
	void deletePatientById(Long id);

}
