package kickstart.rezepterfassung;

import jakarta.persistence.*;
import kickstart.leistungskatalog.Leistung;

@Entity
public class LeistungInfo {
	@ManyToOne
	private Leistung leistung;
	private int maxCount;//The number of the service units which can be provided

	//The number of the service units for which an open appointment exists
	//The following must be always true: openAppointmentsCount <= (maxCount - actCount)
	//No reservation for a Leistung should be
	// possible if: maxCount - ( actCount + openAppointmentsCount ) == 0
	private int openAppointmentsCount;
	private int actCount;//The number of the service units which have been already provided, must be always under maxCount
	private @Id
	@GeneratedValue Long id;
	public LeistungInfo(){}

	public LeistungInfo(Leistung leistung, int maxCount, int openAppointmentsCount, int actCount) {
		this.leistung = leistung;
		this.maxCount = maxCount;
		this.openAppointmentsCount = openAppointmentsCount;
		this.actCount = actCount;
	}

	public int getOpenAppointmentsCount() {
		return openAppointmentsCount;
	}

	public void setOpenAppointmentsCount(int openAppointmentsCount) {
		this.openAppointmentsCount = openAppointmentsCount;
	}

	public long getId(){
		return id;
	}

	public Leistung getLeistung() {
		return leistung;
	}

	public void setLeistung(Leistung leistung) {
		this.leistung = leistung;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public int getActCount() {
		return actCount;
	}

	public void setActCount(int actCount) {
		this.actCount = actCount;
	}

	public long getPriceInCent(){
		return (long)Math.ceil(leistung.getPrise()*100);
	}
	public String getPriceInEuroStr() {
		return String.format("%d,%s EUR",getPriceInCent()/100,
				(getPriceInCent()%100==0)? "00" : (getPriceInCent()%100==0)+"");
	}
	public long getActSumInCent(){
		return getPriceInCent()*getActCount();
	}
	public String getActSumInEuroStr(){
		return String.format("%d,%s EUR",getActSumInCent()/100,
				(getActSumInCent()%100==0)? "00" : (getActSumInCent()%100==0)+"");
	}
}
