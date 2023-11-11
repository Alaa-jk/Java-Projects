package kickstart.rezepterfassung;

import kickstart.leistungskatalog.Leistung;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface LeistungInfoRepository  extends CrudRepository<LeistungInfo,Long> {
}
