package mps.auftrag.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import mps.kunde.entities.Kunde;

@Entity
public class Auftrag {

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	private Kunde kunde;
	
	@OneToMany(mappedBy="parent",cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.EAGER)
	private List<Auftragsposition> posten = new ArrayList<Auftragsposition>();

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Kunde getKunde(){
		return kunde;
	}

	public void setKunde(Kunde kunde) {
		this.kunde = kunde;
	}
	
	public List<Auftragsposition> getPosten() {
		return posten;
	}

	public void setPosten(List<Auftragsposition> posten) {
		this.posten = posten;
	}

	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Auftrag)) return false;
		if(id==null) throw new RuntimeException("Entity has not been saved.");
		return id.equals(((Auftrag)other).getId());
	}
	
	@Override
	public int hashCode() {
		if(id==null) throw new RuntimeException("Entity has not been saved.");
		return id.hashCode();
	}

	@Override
	public String toString() {
		return "Auftrag [id=" + id + ", kunde=" + kunde + ", posten=" + posten + "]";
	}
	
	
}
