package kickstart.personalverwaltung;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonalTest {

    @Test
    public void testPersonalConstructor() {
        // Create a Personal object
        Personal personal = new Personal(
                "John",
                "Doe",
                LocalDate.of(1990, 9, 9),
                "Full-time",
                "Manager",
                "Address",
                "username",
                "password",
                "10000",
                "IBAN",
                "ROLE_USER"
        );

        // Verify the object's properties
        assertEquals("John", personal.getFirstname());
        assertEquals("Doe", personal.getLastname());
        assertEquals(LocalDate.of(1990, 9, 9), personal.getBirthdate());
        assertEquals("Full-time", personal.getFullOrParttime());
        assertEquals("Manager", personal.getPosition());
        assertEquals("Address", personal.getAddress());
        assertEquals("username", personal.getUsername());
        assertEquals("password", personal.getPassword());
        assertEquals("10000", personal.getSalary());
        assertEquals("IBAN", personal.getIban());
        assertEquals("ROLE_USER", personal.getRoles());
    }

    @Test
    public void testPersonalSetterMethods() {
        // Create a Personal object
        Personal personal = new Personal();

        // Set the object's properties using setter methods
        personal.setFirstname("John");
        personal.setLastname("Doe");
        personal.setBirthdate(LocalDate.of(1990, 9, 9));
        personal.setFullOrParttime("Full-time");
        personal.setPosition("Manager");
        personal.setAddress("Address");
        personal.setUsername("username");
        personal.setPassword("password");
        personal.setSalary("10000");
        personal.setIban("IBAN");
        personal.setRoles("ROLE_USER");

        // Verify the object's properties
        assertEquals("John", personal.getFirstname());
        assertEquals("Doe", personal.getLastname());
        assertEquals(LocalDate.of(1990, 9, 9), personal.getBirthdate());
        assertEquals("Full-time", personal.getFullOrParttime());
        assertEquals("Manager", personal.getPosition());
        assertEquals("Address", personal.getAddress());
        assertEquals("username", personal.getUsername());
        assertEquals("password", personal.getPassword());
        assertEquals("10000", personal.getSalary());
        assertEquals("IBAN", personal.getIban());
        assertEquals("ROLE_USER", personal.getRoles());
    }
}