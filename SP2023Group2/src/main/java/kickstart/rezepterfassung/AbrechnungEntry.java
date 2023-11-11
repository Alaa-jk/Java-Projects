package kickstart.rezepterfassung;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class AbrechnungEntry {
	private @Id
	@GeneratedValue Long id;
	private long patientId;
	private String patientFirstname;
	private String patientLastname;
	private LocalDate patientBirthdate;
	private long personalId;
	private String personalFirstname;
	private String personalLastname;
	private LocalDate personalBirthdate;
	private long rezeptId;
	private long leistungId;
	private String leistungName;

	//Price of the privided service in cent
	private long leistungPrice;
	private long leistungCount;
	private boolean isServiceProvided;
	private String approvalNumber;
	private String diagnosisNumber;
	private String province;
	private String personalGroup;
	private String healthInsurance;
	private boolean archived;//If the referrenced patient/personal/service was deleted
	private boolean confirmed;//"Zahlung bestätigt?"
	public AbrechnungEntry(){}

	public long getId() { return id; }
	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public String getPatientFirstname() {
		return patientFirstname;
	}

	public void setPatientFirstname(String patientFirstname) {
		this.patientFirstname = patientFirstname;
	}

	public String getPatientLastname() {
		return patientLastname;
	}

	public void setPatientLastname(String patientLastname) {
		this.patientLastname = patientLastname;
	}

	public LocalDate getPatientBirthdate() {
		return patientBirthdate;
	}

	public void setPatientBirthdate(LocalDate patientBirthdate) {
		this.patientBirthdate = patientBirthdate;
	}

	public long getPersonalId() {
		return personalId;
	}

	public void setPersonalId(long personalId) {
		this.personalId = personalId;
	}

	public String getPersonalFirstname() {
		return personalFirstname;
	}

	public void setPersonalFirstname(String personalFirstname) {
		this.personalFirstname = personalFirstname;
	}

	public String getPersonalLastname() {
		return personalLastname;
	}

	public void setPersonalLastname(String personalLastname) {
		this.personalLastname = personalLastname;
	}

	public LocalDate getPersonalBirthdate() {
		return personalBirthdate;
	}

	public void setPersonalBirthdate(LocalDate personalBirthdate) {
		this.personalBirthdate = personalBirthdate;
	}

	public long getRezeptId() {
		return rezeptId;
	}

	public void setRezeptId(long rezeptId) {
		this.rezeptId = rezeptId;
	}


	public String getLeistungName() {
		return leistungName;
	}

	public void setLeistungName(String leistungName) {
		this.leistungName = leistungName;
	}

	public long getLeistungPrice() {
		return leistungPrice;
	}

	public void setLeistungPrice(long leistungPrice) {
		this.leistungPrice = leistungPrice;
	}

	public long getLeistungCount() {
		return leistungCount;
	}

	public void setLeistungCount(long leistungCount) {
		this.leistungCount = leistungCount;
	}

	public String getApprovalNumber() {
		return approvalNumber;
	}

	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}

	public String getDiagnosisNumber() {
		return diagnosisNumber;
	}

	public void setDiagnosisNumber(String diagnosisNumber) {
		this.diagnosisNumber = diagnosisNumber;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPersonalGroup() {
		return personalGroup;
	}

	public void setPersonalGroup(String personalGroup) {
		this.personalGroup = personalGroup;
	}

	public String getHealthInsurance() {
		return healthInsurance;
	}

	public void setHealthInsurance(String healthInsurance) {
		this.healthInsurance = healthInsurance;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public boolean isServiceProvided() {
		return isServiceProvided;
	}

	public void setServiceProvided(boolean serviceProvided) {
		isServiceProvided = serviceProvided;
	}

	public long getLeistungId() {
		return leistungId;
	}

	public void setLeistungId(long leistungId) {
		this.leistungId = leistungId;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public AbrechnungEntry(String patientFirstname, String patientLastname, LocalDate patientBirthdate, long patientId,
						   String personalFirstname, String personalLastname, LocalDate personalBirthdate, long personalId,
						   long rezeptId, long leistungId, String leistungName, long leistungPrice, long leistungCount,
						   boolean isServiceProvided,
						   String approvalNumber, String diagnosisNumber, String province,
						   String personalGroup, String healthInsurance, boolean archived) {
		this.patientFirstname = patientFirstname;
		this.patientLastname = patientLastname;
		this.patientBirthdate = patientBirthdate;
		this.patientId = patientId;
		this.personalFirstname = personalFirstname;
		this.personalLastname = personalLastname;
		this.personalBirthdate = personalBirthdate;
		this.personalId = personalId;
		this.rezeptId = rezeptId;
		this.leistungId = leistungId;
		this.leistungName = leistungName;
		this.leistungPrice = leistungPrice;
		this.leistungCount = leistungCount;
		this.isServiceProvided = isServiceProvided;
		this.approvalNumber = approvalNumber;
		this.diagnosisNumber = diagnosisNumber;
		this.province = province;
		this.personalGroup = personalGroup;
		this.healthInsurance = healthInsurance;
		this.archived = archived;
		this.confirmed = false;
	}
	public String getPriceInEuroStr() {
		return String.format("%d,%s EUR",getLeistungPrice()/100,
				(getLeistungPrice()%100==0)? "00" : (getLeistungPrice()%100==0)+"");
	}
}
