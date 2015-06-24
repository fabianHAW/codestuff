package mps.produkt.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Produkt {

	@Id
	@GeneratedValue
	private Long id;
	
	private String name;

	public Long getId() {
		return id;
	}
	
	public String getName(){
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Produkt)) return false;
		if(id==null) throw new RuntimeException("Entity has not been saved.");
		return id.equals(((Produkt)other).getId());
	}
	
	@Override
	public int hashCode() {
		if(id==null) throw new RuntimeException("Entity has not been saved.");
		return id.hashCode();
	}

	@Override
	public String toString() {
		return "Produkt [id=" + id + ", name=" + name + "]";
	}
	
}
