package kickstart.patientenverwaltung;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
public class PatientTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testPatientEntity() {
        // Create a test patient
        LocalDate birthdate = LocalDate.of(1990, 1, 1);
        Patient patient = new Patient("John", "Doe", birthdate, "Insurance", "123456789", "123 Street");

        // Persist the patient
        entityManager.persist(patient);
        entityManager.flush();
        entityManager.clear();

        // Retrieve the patient from the database
        Patient retrievedPatient = entityManager.find(Patient.class, patient.getId());

        // Perform assertions
        assertEquals("John", retrievedPatient.getFirstname());
        assertEquals("Doe", retrievedPatient.getLastname());
        assertEquals(birthdate, retrievedPatient.getBirthdate());
        assertEquals("Insurance", retrievedPatient.getHealthInsurance());
        assertEquals("123456789", retrievedPatient.getHealthInsuranceNumber());
        assertEquals("123 Street", retrievedPatient.getAddress());
    }

    @Test
    public void testPatientEntityDefaults() {
        // Create a test patient without setting any values
        Patient patient = new Patient();

        // Perform assertions
        assertNull(patient.getFirstname());
        assertNull(patient.getLastname());
        assertNull(patient.getBirthdate());
        assertNull(patient.getHealthInsurance());
        assertNull(patient.getHealthInsuranceNumber());
        assertNull(patient.getAddress());
    }
}