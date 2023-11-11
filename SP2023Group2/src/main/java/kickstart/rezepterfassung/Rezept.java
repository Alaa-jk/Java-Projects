package kickstart.rezepterfassung;
import jakarta.persistence.*;
import kickstart.patientenverwaltung.Patient;
import kickstart.personalverwaltung.Personal;

import java.util.List;

@Entity
public class Rezept {
	private static final String[] statusLookup
			= { "OPEN","FINISHED"};
	private @Id @GeneratedValue Long id;
	@ManyToOne
	private Patient patient;
	@ManyToOne
	private Personal personal;
	@OneToMany(cascade = CascadeType.ALL)
	private List<LeistungInfo> leistungInfoList;
	private int status;
	private String approvalNumber, diagnosisNumber,
			province, personalGroup, healthInsurance;

	@SuppressWarnings("unused")
	public Rezept() {}

	public Rezept(Patient patient, Personal personal,
				  List<LeistungInfo> leistungInfoList,
				  int status, String approvalNumber,
				  String diagnosisNumber,
				  String province, String personalGroup) {
		this.patient = patient;
		this.personal = personal;
		this.leistungInfoList = leistungInfoList;
		this.status = status;
		this.approvalNumber = approvalNumber;
		this.diagnosisNumber = diagnosisNumber;
		this.province = province;
		this.personalGroup = personalGroup;
		this.healthInsurance = patient.getHealthInsurance();
	}

	public Long getId() {
		return id;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Personal getPersonal() {
		return personal;
	}

	public void setPersonal(Personal personal) {
		this.personal = personal;
	}

	public List<LeistungInfo> getLeistungInfoList() {
		return leistungInfoList;
	}

	public void setLeistungInfoList(List<LeistungInfo> leistungInfoList) {
		this.leistungInfoList = leistungInfoList;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusStr () { return statusLookup[status]; }
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

	public String getHealthInsurance(){
		return getPatient().getHealthInsurance();
	}
}
