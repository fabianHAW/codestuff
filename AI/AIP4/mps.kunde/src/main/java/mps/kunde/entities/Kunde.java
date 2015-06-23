package mps.kunde.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Kunde {

	@Id
	@GeneratedValue
	private Long id;
	
	private String stadt;
	
	private String name;
	
	
	protected Kunde(){}

	public Kunde(String name){
		this.name = name;
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

	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Kunde)) return false;
		if(id==null) throw new RuntimeException("Entity has not been saved.");
		return id.equals(((Kunde)other).getId());
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
