package kickstart.leistungskatalog;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
public class Leistung {
	
	private @Id @GeneratedValue Long id;
	private  String name;
	private  double prise;
	
	@Column(name = "dauer")
	private  String dauer;//in minute

	
	public Leistung(String name, double prise, String dauer) {

		this.name = name;
		this.prise = prise;
		this.dauer = dauer;
		
		
	}
		
	public Leistung() {

			
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrise() {
		return prise;
	}

	public void setPrise(double prise) {
		this.prise = prise;
	}

	public String getDauer() {
		return dauer;
	}

	public void setDauer(String dauer) {
		this.dauer = dauer;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	//For the price format in EUR
	public String getPriceInEuroStr(){
		long priceInCent = (long) getPrise()*100;
		return String.format("%d,%s EUR",priceInCent/100,
				(priceInCent%100==0)? "00" : (priceInCent%100==0)+"");
	}
	
}
