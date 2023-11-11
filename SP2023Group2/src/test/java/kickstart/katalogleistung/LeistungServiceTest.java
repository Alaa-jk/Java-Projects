package kickstart.katalogleistung;


import kickstart.leistungskatalog.Leistung;
import kickstart.leistungskatalog.LeistungRepository;
import kickstart.leistungskatalog.LeistungService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LeistungServiceTest {

	@Mock
	private LeistungRepository leistungRepository;

	@InjectMocks
	private LeistungService leistungService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void findLeistungById_shouldReturnLeistungWithGivenId() {
		// Arrange
		Long id = 1L;
		Leistung leistung = new Leistung();
		leistung.setId(id);
		when(leistungRepository.getLeistungbyId(id)).thenReturn(leistung);

		// Act
		Leistung result = leistungService.findLeistungById(id);

		// Assert
		assertEquals(leistung, result);
	}

	@Test
	void getAllLeistungen_shouldReturnAllLeistungen() {
		// Arrange
		List<Leistung> leistungen = new ArrayList<>();
		leistungen.add(new Leistung());
		leistungen.add(new Leistung());
		when(leistungRepository.getAllLeistungen()).thenReturn(leistungen);

		// Act
		List<Leistung> result = leistungService.getAllLeistungen();

		// Assert
		assertEquals(leistungen, result);
	}

	@Test
	void deleteLeistungById_shouldDeleteLeistungWithGivenId() {
		// Arrange
		Long id = 1L;

		// Act
		leistungService.deleteLeistungById(id);

		// Assert
		verify(leistungRepository).deleteById(id);
	}

	@Test
	void updateLeistung_shouldSaveUpdatedLeistung() {
		// Arrange
		Leistung leistung = new Leistung();

		// Act
		leistungService.updateLeistung(leistung);

		// Assert
		verify(leistungRepository).save(leistung);
	}

	@Test
	void createLeistung_shouldSaveNewLeistung() {
		// Arrange
		Leistung leistung = new Leistung();

		// Act
		leistungService.createLeistung(leistung);

		// Assert
		verify(leistungRepository).save(leistung);
	}

	// Weitere Tests für andere Methoden des LeistungService können hier hinzugefügt werden
}
