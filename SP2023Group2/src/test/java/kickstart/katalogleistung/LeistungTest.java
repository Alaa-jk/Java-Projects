package kickstart.katalogleistung;



import kickstart.leistungskatalog.Leistung;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LeistungTest {

	@Test
	void getPriceInEuroStr_shouldReturnPriceFormattedInEuro() {
		// Arrange
		Leistung leistung = new Leistung();
		leistung.setPrise(10.00);

		// Act
		String priceInEuroStr = leistung.getPriceInEuroStr();

		// Assert
		assertEquals("10,00 EUR", priceInEuroStr);
	}

	// Weitere Tests für andere Methoden der Leistungsklasse können hier hinzugefügt werden
}
