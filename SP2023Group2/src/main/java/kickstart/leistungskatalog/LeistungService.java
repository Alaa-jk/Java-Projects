package kickstart.leistungskatalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeistungService {
    private final LeistungRepository leistungRepository;

    @Autowired
    public LeistungService(LeistungRepository leistungRepository) {
        this.leistungRepository = leistungRepository;
    }

    public Leistung findLeistungById(Long id) {
        return leistungRepository.getLeistungbyId(id);
    }

    public List<Leistung> getAllLeistungen() {
        return leistungRepository.getAllLeistungen();
    }

    public void deleteLeistungById(Long id) {
        leistungRepository.deleteById(id);
	
    }
    
 
    public void updateLeistung(Leistung leistung) {
        leistungRepository.save(leistung);
        }
    
    public void createLeistung(Leistung leistung) {
    	leistungRepository.save(leistung);
    	
    }


	public Iterable<Leistung> findAll() {
		return leistungRepository.findAll();
	}
}
