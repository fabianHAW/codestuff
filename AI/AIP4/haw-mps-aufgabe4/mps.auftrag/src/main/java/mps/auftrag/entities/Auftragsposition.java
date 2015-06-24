package mps.auftrag.entities;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import mps.produkt.entities.Produkt;

@Entity
public class Auftragsposition {

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne(optional=false)
	private Auftrag parent;
	
	@ManyToOne(optional=false)
	private Produkt produkt;
	
	private BigDecimal preis;
	
	private int anzahl;
	
	// internal jpa use only.
	protected Auftragsposition(){}
	
	public Auftragsposition(Auftrag parent, Produkt produkt) { // all mandatory properties should go into the constructor
		this.parent = parent;
		this.produkt = produkt;
	}

	public Long getId() {
		return id;
	}
	
	public Produkt getProdukt(){
		return produkt;
	}

	public BigDecimal getPreis() {
		return preis;
	}

	public void setPreis(BigDecimal preis) {
		this.preis = preis;
	}

	public int getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}


	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Auftragsposition)) return false;
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
		return "Auftragsposition [id=" + id + ", parent=" + parent.getId()
				+ ", produkt=" + produkt + ", preis=" + preis + ", anzahl="
				+ anzahl + "]";
	}
	
}
