package kickstart.terminplaner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
@Service
public class TerminService {
	@Autowired
	private TerminplanerRepository terminplanerRepository;
	private static final String[] ROLES = new String[]{"boss","personal","therapeut"};

	/**
	 * Check if the appointment is within the slots 0 (10:00 - 10:20) to 11 (14:20 - 14:40)
	 * possibly false if an appointment begins at 14:20 and has a duration of 2 or 3 units
	 * @param t Termin to validate
	 * @return if the appointment is within the timeslots
	 */
	boolean isWithinTimeslots(Termin t){
		return (t.getBeginSlotIndex() + t.getCountUnit() - 1) < Termin.TIMESLOTS;
	}

	/**
	 * Check if an appointment has a collision with another appointment reserved
	 * @param t Termin to validate
	 * @return if the appointment has a collision with another
	 */
	boolean hasCollision(Termin t){

		for(Termin comparison : terminplanerRepository.findByDate(t.getDate())){
			if(comparison.getStatus() == 0 && t.getStatus() == 0 &&
					!(t.getBeginSlotIndex()+t.getCountUnit() <= comparison.getBeginSlotIndex() ||
						comparison.getBeginSlotIndex()+comparison.getCountUnit() <= t.getBeginSlotIndex()))
				return true;
		}
		return false;
	}

	/**
	 * Recommend an alternative appointment for an appointment with a certain duration
	 * @param t Termin
	 * @return recommended date and the beginning index for an alternative as a Map
	 */
	Map<LocalDate, Integer> recommend (Termin t) {
		boolean found = true;
		for(int i=0; i<Termin.MAX_DAYS; i++) {
			for(int j=0; j<Termin.TIMESLOTS; j++) {
				found = true;
				for(Termin comparison : terminplanerRepository.findByDate(t.getDate().plusDays(i))){
					if (!(comparison.getBeginSlotIndex() + comparison.getCountUnit() <= j
							|| j + t.getCountUnit() <= comparison.getBeginSlotIndex())
							&& comparison.getStatus() == 0) {
						found = false; //An appointment is found which has a collision with the actual recommended appointment
						break;
					}
				}
				if(found){
					return Map.of(t.getDate().plusDays(i), j);
				}
			}
		}
		return null;
	}
}
