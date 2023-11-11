package kickstart.personalverwaltung;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class PersonalRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonalRepository personalRepository;

    @Test
    public void testGetAllPersonal() {
        // Create test data
        Personal personal1 = new Personal("John", "Doe", LocalDate.of(1990, 9, 1), "Full-Time", "Position 1", "Address 1", "john", "password", "1000", "IBAN1", "ROLE1");
        Personal personal2 = new Personal("Jane", "Smith", LocalDate.of(1985, 6, 15), "Part-Time", "Position 2", "Address 2", "jane", "password", "2000", "IBAN2", "ROLE2");
        entityManager.persist(personal1);
        entityManager.persist(personal2);
        entityManager.flush();

        // Call the repository method
        List<Personal> personalList = personalRepository.getAllPersonal();

        // Assertions
        assertEquals(2, personalList.size());
        assertTrue(personalList.contains(personal1));
        assertTrue(personalList.contains(personal2));
    }

    @Test
    public void testGetPersonalByUsername() {
        // Create test data
        Personal personal = new Personal("John", "Doe", LocalDate.of(1990, 9, 1), "Full-Time", "Position 1", "Address 1", "john", "password", "1000", "IBAN1", "ROLE1");
        entityManager.persist(personal);
        entityManager.flush();

        // Call the repository method
        Optional<Personal> foundPersonal = personalRepository.getPersonalByusername("john");

        // Assertions
        assertTrue(foundPersonal.isPresent());
        assertEquals(personal, foundPersonal.get());
    }
}