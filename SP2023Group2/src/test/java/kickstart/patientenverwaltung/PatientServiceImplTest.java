package kickstart.patientenverwaltung;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PatientServiceImplTest {

    private PatientRepository patientRepository;
    private PatientService patientService;

    @BeforeEach
    public void setup() {
        patientRepository = Mockito.mock(PatientRepository.class);
        patientService = new PatientServiceImpl(patientRepository);
    }

    @Test
    public void testGetAllPatients() {
        // Create a list of patients
        List<Patient> patients = new ArrayList<>();
        LocalDate birthdate = LocalDate.of(1990, 1, 1);
        patients.add(new Patient("John", "Doe", birthdate, "Insurance", "123456789", "123 Street"));
        patients.add(new Patient("Jane", "Smith",birthdate, "Insurance", "123456789", "123 Street"));

        // Mock the findAll method of the repository to return the list of patients
        when(patientRepository.findAll()).thenReturn(patients);

        // Call the getAllPatients method of the service
        List<Patient> result = patientService.getAllPatients();

        // Verify that the findAll method of the repository is called
        verify(patientRepository, times(1)).findAll();

        // Verify the result
        assertEquals(patients, result);
    }

    @Test
    public void testSavePatient() {
        // Create a patient
    	LocalDate birthdate = LocalDate.of(1990, 1, 1);
        Patient patient = new Patient("John", "Doe", birthdate, "Insurance", "123456789", "123 Street");

        // Mock the save method of the repository to return the saved patient
        when(patientRepository.save(patient)).thenReturn(patient);

        // Call the savePatient method of the service
        Patient result = patientService.savePatient(patient);

        // Verify that the save method of the repository is called
        verify(patientRepository, times(1)).save(patient);

        // Verify the result
        assertEquals(patient, result);
    }

    @Test
    public void testGetPatientById() {
        // Create a patient
    	LocalDate birthdate = LocalDate.of(1990, 1, 1);
        Patient patient = new Patient("John", "Doe", birthdate, "Insurance", "123456789", "123 Street");
        patient.setId(1L);

        // Mock the findById method of the repository to return the patient
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        // Call the getPatientById method of the service
        Patient result = patientService.getPatientById(1L);

        // Verify that the findById method of the repository is called
        verify(patientRepository, times(1)).findById(1L);

        // Verify the result
        assertEquals(patient, result);
    }

    @Test
    public void testUpdatePatient() {
        // Create a patient
    	LocalDate birthdate = LocalDate.of(1990, 1, 1);
        Patient patient = new Patient("John", "Doe", birthdate, "Insurance", "123456789", "123 Street");
        patient.setId(1L);

        // Mock the save method of the repository to return the updated patient
        when(patientRepository.save(patient)).thenReturn(patient);

        // Call the updatePatient method of the service
        Patient result = patientService.updatePatient(patient);

        // Verify that the save method of the repository is called
        verify(patientRepository, times(1)).save(patient);

        // Verify the result
        assertEquals(patient, result);
    }

    @Test
    public void testDeletePatientById() {
    	
    	 // Create a patient
    	LocalDate birthdate = LocalDate.of(1990, 1, 1);
        Patient patient = new Patient("John", "Doe", birthdate, "Insurance", "123456789", "123 Street");
        patient.setId(1L);
        
        // Call the deletePatientById method of the service
        patientService.deletePatientById(1L);

        // Verify that the deleteById method of the repository is called
        verify(patientRepository, times(1)).deleteById(1L);
    }
}
