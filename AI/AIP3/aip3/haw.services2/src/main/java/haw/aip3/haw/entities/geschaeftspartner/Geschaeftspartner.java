package haw.aip3.haw.entities.geschaeftspartner;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class Geschaeftspartner {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String stadt;
	
	@Column
	private String name;
	
	
	protected Geschaeftspartner(){}

	public Geschaeftspartner(String name){
		this.name = name;
	}
	
	public Geschaeftspartner(String name, String stadt){
		this.name = name;
		this.stadt = stadt;
	}
	

	public Long getId() {
		return id;
	}

	public String getStadt() {
		return stadt;
	}

	public void setStadt(String stadt) {
		this.stadt = stadt;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Geschaeftspartner)) return false;
		if(id==null) throw new RuntimeException("Entity has not been saved.");
		return id.equals(((Geschaeftspartner)other).getId());
	}
	
	@Override
	public int hashCode() {
		if(id==null) throw new RuntimeException("Entity has not been saved.");
		return id.hashCode();
	}

	@Override
	public String toString() {
		return "Kunde [id=" + id + ", stadt=" + stadt + "]";
	}


}
