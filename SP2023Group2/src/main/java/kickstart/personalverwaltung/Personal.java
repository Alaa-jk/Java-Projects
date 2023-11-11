package kickstart.personalverwaltung;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


@Entity
public class Personal {
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "firstname")
    private String firstname;
	
	@Column(name = "lastname")
    private String lastname;

    @Column(name = "fullOrParttime")
    private String fullOrParttime;

    @Column(name = "address")
    private String address;

    @Column(name = "birthdate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")                  
    private LocalDate birthdate;

    @Column(name = "position")
    private String position;
	
	@Column(name = "username")
    private String username;
	
	@Column(name = "password")
    private String password;
	

	
	@Column(name = "salary")
    private String salary;
    
	@Column(name = "iban")
    private String iban;
	
	
	@Column(name = "roles")
    private String roles;
   

	 Personal() {}         

	public Personal(String firstname,String lastname,LocalDate birthdate, String fullOrParttime, String position,String address, String username, String password,String salary,String iban,String roles) 
	{   super();
		this.birthdate = birthdate;
		this.firstname = firstname;
		this.lastname = lastname;
		this.position = position;
		this.address = address;
		this.fullOrParttime = fullOrParttime;
		this.username = username;
		this.password = password;
		this.salary = salary;
		this.iban = iban;
		this.roles=roles;
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

	public String getPosition(){
		return position;
	}
	
	public void  setPosition(String position) {
		this.position = position;
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


	public String getFullOrParttime(){
		return fullOrParttime;
	}
	
	public void  setFullOrParttime(String fullOrParttime) {
		this.fullOrParttime = fullOrParttime;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void  setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void  setPassword(String password) {
		this.password = password;
	}

	
	public String getSalary() {
		return salary;
	}
	
	public void  setSalary(String salary) {
		this.salary = salary;
	}
	
	public String getIban() {
		return iban;
	}
	
	public void  setIban(String iban) {
		this.iban = iban;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	
	
	
}

