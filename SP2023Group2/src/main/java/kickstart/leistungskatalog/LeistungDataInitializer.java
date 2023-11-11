package kickstart.leistungskatalog;


import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;





@Component
@Order(3)
public class LeistungDataInitializer  implements ApplicationRunner  {
	
	 private final LeistungRepository leistungRepository;
	
	  public LeistungDataInitializer(LeistungRepository leistungRepository) {
	        this.leistungRepository = leistungRepository;
	    }

		
		@Override
		public void run(ApplicationArguments args) throws Exception {
			List.of(
					new Leistung ("Bewegungstherapie",200.0,
							"1"),
					new Leistung("Atemtherapie",
							300.0,
							"1")
			).forEach(leistungRepository::save);
		}
	
}
