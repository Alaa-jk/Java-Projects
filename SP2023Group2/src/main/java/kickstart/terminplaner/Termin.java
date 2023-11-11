package kickstart.terminplaner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import kickstart.leistungskatalog.Leistung;
import kickstart.patientenverwaltung.Patient;
import kickstart.personalverwaltung.Personal;
import kickstart.rezepterfassung.Rezept;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public
class Termin {
	private static final String[] statusLookup
			= { "OPEN","FINISHED"};
	//Next thirty days only for which it is possible to make an appointment
	public static final int MAX_DAYS = 180;
	public static final int TIMEUNIT = 20; //20 minutes as time unit
	public static final int TIMESLOTS = 12; //12 time units per day
	/*
	 * Timeslots
	 * Slot        begin
	 * 0           10:00
	 * 1           10:20
	 * 2           10:40
	 * 3           11:00
	 * 4           11:20
	 * 5           11:40
	 * 6           12:00
	 * 7           13:00
	 * 8           13:20
	 * 9           13:40
	 * 10          14:00
	 * 11          14:20
	 *
	 * */
	@ManyToOne
	private Patient patient;
	@ManyToOne
	private Personal therapist;
	@ManyToOne
	private Rezept rezept;
	@ManyToOne
	private Leistung leistung;
	private int status;
	private int beginSlotIndex;
	//The number of provided service units (LeistungInfo.actCount) of rezept
	//must be (actCount - countUnit)
	//bei status change from 0 to 1
	private int countUnit;

	public String getTime(){
		return timeslots[beginSlotIndex];
	}

	public int getDuration(){
		return countUnit * TIMEUNIT;
	}
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;  //the date of an appointment
	private @Id @GeneratedValue Long id;

	public static final String[] timeslots = new String[] {
			"10:00", "10:20", "10:40", "11:00", "11:20", "11:40", "12:00",
			"13:00", "13:20", "13:40", "14:00", "14:20", "14:40", "15:00"
	};

	@SuppressWarnings("unused")
	public Termin() {}

	public Termin(Patient patient, Personal therapist,
				  Rezept rezept, Leistung leistung,
				  int status, int beginSlotIndex,
				  int countUnit, LocalDate date) {
		this.patient = patient;
		this.therapist = therapist;
		this.rezept = rezept;
		this.leistung = leistung;
		this.status = status;
		this.beginSlotIndex = beginSlotIndex;
		this.countUnit = countUnit;
		this.date = date;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Personal getTherapist() {
		return therapist;
	}

	public void setTherapist(Personal therapist) {
		this.therapist = therapist;
	}

	public Rezept getRezept() {
		return rezept;
	}

	public void setRezept(Rezept rezept) {
		this.rezept = rezept;
	}

	public Leistung getLeistung() {
		return leistung;
	}

	public void setLeistung(Leistung leistung) {
		this.leistung = leistung;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getBeginSlotIndex() {
		return beginSlotIndex;
	}

	public void setBeginSlotIndex(int beginSlotIndex) {
		this.beginSlotIndex = beginSlotIndex;
	}

	public int getCountUnit() {
		return countUnit;
	}

	public void setCountUnit(int countUnit) {
		this.countUnit = countUnit;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getDateStr() { return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); }

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
