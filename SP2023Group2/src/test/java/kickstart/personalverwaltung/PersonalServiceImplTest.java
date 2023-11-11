package kickstart.personalverwaltung;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import kickstart.patientenverwaltung.PatientRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PersonalServiceImplTest {

    @Mock
    private PersonalRepository personalRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PersonalServiceImpl personalService;

    @Test
    public void testGetAllPersonals() {
        // Create test data
        Personal personal1 = new Personal("John", "Doe", LocalDate.of(1990, 9, 1), "Full-Time", "Position 1", "Address 1", "john", "password", "1000", "IBAN1", "ROLE1");
        Personal personal2 = new Personal("Jane", "Smith", LocalDate.of(1985, 6, 15), "Part-Time", "Position 2", "Address 2", "jane", "password", "2000", "IBAN2", "ROLE2");
        List<Personal> personalList = Arrays.asList(personal1, personal2);

        // Mock the repository method
        when(personalRepository.getAllPersonal()).thenReturn(personalList);

        // Call the service method
        List<Personal> result = personalService.getAllPersonals();

        // Assertions
        assertEquals(2, result.size());
        assertTrue(result.contains(personal1));
        assertTrue(result.contains(personal2));

        // Verify the repository method was called
        verify(personalRepository, times(1)).getAllPersonal();
    }

    @Test
    public void testSavePersonal() {
        // Create test data
        Personal personal = new Personal("John", "Doe", LocalDate.of(1990, 9, 1), "Full-Time", "Position 1", "Address 1", "john", "password", "1000", "IBAN1", "ROLE1");

        // Mock the repository method
        when(personalRepository.save(personal)).thenReturn(personal);

        // Call the service method
        Personal result = personalService.savePersonal(personal);

        // Assertions
        assertNotNull(result);
        assertEquals(personal, result);

        // Verify the repository method was called
        verify(personalRepository, times(1)).save(personal);
    }

    @Test
    public void testGetPersonalById() {
        // Create test data
        Long id = 1L;
        Personal personal = new Personal("John", "Doe", LocalDate.of(1990, 9, 1), "Full-Time", "Position 1", "Address 1", "john", "password", "1000", "IBAN1", "ROLE1");

        // Mock the repository method
        when(personalRepository.findById(id)).thenReturn(Optional.of(personal));

        // Call the service method
        Personal result = personalService.getPersonalById(id);

        // Assertions
        assertNotNull(result);
        assertEquals(personal, result);

        // Verify the repository method was called
        verify(personalRepository, times(1)).findById(id);
    }

    @Test
    public void testUpdatePersonal() {
        // Create test data
        Personal personal = new Personal("John", "Doe", LocalDate.of(1990, 9, 1), "Full-Time", "Position 1", "Address 1", "john", "password", "1000", "IBAN1", "ROLE1");

        // Mock the repository method
        when(personalRepository.save(personal)).thenReturn(personal);

        // Call the service method
        Personal result = personalService.updatePersonal(personal);

        // Assertions
        assertNotNull(result);
        assertEquals(personal, result);

        // Verify the repository method was called
        verify(personalRepository, times(1)).save(personal);
    }

    @Test
    public void testDeletePersonalById() {
        // Create test data
        Long id = 1L;

        // Call the service method
        personalService.deletePersonalById(id);

        // Verify the repository method was called
        verify(personalRepository, times(1)).deleteById(id);
    }

    @Test
    public void testLoadUserByUsername_ValidUsername() {
        // Create test data
        String username = "john";
        Personal personal = new Personal("John", "Doe", LocalDate.of(1990, 9, 1), "Full-Time", "Position 1", "Address 1", "john", "password", "1000", "IBAN1", "ROLE1");
        Optional<Personal> optionalPersonal = Optional.of(personal);

        // Mock the repository method
        when(personalRepository.getPersonalByusername(username)).thenReturn(optionalPersonal);

        // Call the service method
        UserDetails result = personalService.loadUserByUsername(username);

        // Assertions
        assertNotNull(result);
        assertTrue(result instanceof PersonalProncipile);
        assertEquals(personal.getUsername(), result.getUsername());

        // Verify the repository method was called
        verify(personalRepository, times(1)).getPersonalByusername(username);
    }

    @Test
    public void testLoadUserByUsername_InvalidUsername() {
        // Create test data
        String username = "john";

        // Mock the repository method to return empty optional
        when(personalRepository.getPersonalByusername(username)).thenReturn(Optional.empty());

        // Call the service method and assert that it throws UsernameNotFoundException
        assertThrows(UsernameNotFoundException.class, () -> personalService.loadUserByUsername(username));

        // Verify the repository method was called
        verify(personalRepository, times(1)).getPersonalByusername(username);
    }
}