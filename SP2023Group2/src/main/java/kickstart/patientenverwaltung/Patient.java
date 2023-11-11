package kickstart.patientenverwaltung;

import jakarta.persistence.*;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name="patients")
//DEV:
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Patient {
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	//DEV:
	//@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	
	@Column(name = "firstname")
    private String firstname;
	
	@Column(name = "lastname")
    private String lastname;

    @Column(name = "healthInsurance")
    private String healthInsurance;

    @Column(name = "address")
    private String address;

    @Column(name = "birthdate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")                  
    private LocalDate birthdate;

    @Column(name = "healthInsuranceNumber")
    private String healthInsuranceNumber;

   

	 Patient() {}         

	public Patient(String firstname,String lastname,LocalDate birthdate, String healthInsurance, String healthInsuranceNumber,String address) 
	{
		this.birthdate = birthdate;
		this.firstname = firstname;
		this.lastname = lastname;
		this.healthInsuranceNumber = healthInsuranceNumber;
		this.address = address;
		this.healthInsurance = healthInsurance;
	}

	public Long getId(){
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFirstname(){
		return firstname;
	}
	
	public void  setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getLastname(){
		return lastname;
	}
	
	public void  setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getHealthInsuranceNumber(){
		return healthInsuranceNumber;
	}
	
	public void  setHealthInsuranceNumber(String healthInsuranceNumber) {
		this.healthInsuranceNumber = healthInsuranceNumber;
	}


	public String getAddress() {
		return address;
	}
	
	public void  setAddress(String address) {
		this.address = address;
	}


	public LocalDate getBirthdate() {
		return birthdate;
	}
	
	public void  setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}


	public String getHealthInsurance(){
		return healthInsurance;
	}
	
	public void  setHealthInsurance(String healthInsurance) {
		this.healthInsurance = healthInsurance;
	}
}
