package kickstart.katalogleistung;



import kickstart.leistungskatalog.KatalogleistungenController;
import kickstart.leistungskatalog.Leistung;
import kickstart.leistungskatalog.LeistungService;

import kickstart.rezepterfassung.LeistungInfoRepository;
import kickstart.rezepterfassung.RezeptManagement;
import kickstart.rezepterfassung.RezeptRepository;
import kickstart.terminplaner.TerminplanerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedList;

import static org.mockito.Mockito.*;

class KatalogleistungenControllerTest {

	@Mock
	private LeistungInfoRepository leistungInfoRepository;
	@Mock
	private RezeptRepository rezeptRepository;
	@Mock
	private TerminplanerRepository terminplanerRepository;
	@Mock
	private RezeptManagement rezeptManagement;
	@Mock
	private LeistungService leistungService;

	@Mock
	private Model model;
	@Mock
	private RedirectAttributes redirectAttributes;
	@Mock
	private Errors errors;

	@InjectMocks
	private KatalogleistungenController controller;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void getAllLeistungen_shouldAddLeistungListToModelAndReturnViewName() {
		// Arrange
		when(leistungService.getAllLeistungen()).thenReturn(new LinkedList<>());

		// Act
		String viewName = controller.getAllLeistungen(model);

		// Assert
		verify(model).addAttribute("LeistungList", new LinkedList<>());
		assert viewName.equals("katalogleistungen");
	}
/* Delete-Test in a separate Unittest file (katalogleistung.KatalogleistungenControllerTest2)
	@Test
	void leistungdelete_shouldDeleteLeistungAndAddFlashAttributeAndReturnViewName() {
		// Arrange
		Long id = 1L;

		// Act
		String viewName = controller.leistungdelete(model, id, redirectAttributes);

		// Assert
		verify(rezeptManagement).deleteLeistung(id);
		verify(redirectAttributes).addFlashAttribute("messageDelete", "Gelöscht");
		assert viewName.equals("deleted-view-name");
	}
*/
	@Test
	void createLeistungen_shouldCreateLeistungAndAddFlashAttributeAndRedirect() {
		// Arrange
		Leistung leistung = new Leistung();

		// Act
		String redirectUrl = controller.createLeistungen(leistung, errors, redirectAttributes);

		// Assert
		verify(leistungService).createLeistung(leistung);
		verify(redirectAttributes).addFlashAttribute("message", "Erfolgreich!");
		assert redirectUrl.equals("redirect:/Katalogleistungen");
	}

	// Weitere Tests für die restlichen Methoden des Controllers können hier hinzugefügt werden
}
