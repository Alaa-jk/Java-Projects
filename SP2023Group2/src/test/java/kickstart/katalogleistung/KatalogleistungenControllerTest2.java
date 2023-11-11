package kickstart.katalogleistung;

import kickstart.leistungskatalog.KatalogleistungenController;
import kickstart.leistungskatalog.Leistung;
import kickstart.leistungskatalog.LeistungRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class KatalogleistungenControllerTest2 {
	@Autowired
	private KatalogleistungenController controller;
	@Autowired
	private LeistungRepository repository;
	@Test
	@WithMockUser(authorities = {"boss"})
	void leistungdelete(){
		Leistung leistung = new Leistung("",200,"");
		repository.save(leistung);
		controller.leistungdelete(new ExtendedModelMap(),leistung.getId(),new RedirectAttributesModelMap());
		assertThat(repository.getLeistungbyId(leistung.getId())).isNull();
	}
}
